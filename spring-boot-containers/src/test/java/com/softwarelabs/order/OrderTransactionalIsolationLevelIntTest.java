package com.softwarelabs.order;

import com.softwarelabs.config.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@Slf4j
/**
 - Spring transaction management use-case with order domain.
 - What is the default isolation level of the Spring Transaction Manager(transactionHelper -> transactionTemplate -> PlatformTransactionManager(TransactionManager))
 - Default isolation level -> https://docs.spring.io/spring-framework/docs/4.2.x/spring-framework-reference/html/transaction.html#transaction-strategies
 - Spring Doc default isolation level -> https://docs.spring.io/spring-framework/docs/5.0.x/javadoc-api/org/springframework/transaction/annotation/Isolation.html#DEFAULT
 - Spring Doc DEFAULT -> https://docs.spring.io/spring-framework/docs/4.1.5.RELEASE/spring-framework-reference/html/transaction.html#transaction-declarative-attransactional-settings
 - What is the default isolation level of the Postgres database? -> Default isolation level
 - https://www.postgresql.org/docs/current/transaction-iso.html#:~:text=Read%20Committed%20is%20the%20default,query%20execution%20by%20concurrent%20transactions.
 * */
public class OrderTransactionalIsolationLevelIntTest extends BaseIntegrationTest {

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    TransactionTemplate transactionTemplate;

    @Autowired
    OrderService orderService;


    @Test
    public void testReadCommittedIsolationLevel_withSingleTransaction() {
        UUID orderId = saveOrder();
        ExecutorService executorService = Executors.newFixedThreadPool(1);
        executorService.execute(thread1(orderId));
        gracefullyShutdown(executorService);
    }

    @Test
    public void testReadCommittedIsolationLevel_withMultipleThreads() {
        UUID orderId = saveOrder();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        executorService.execute(thread2(orderId));
        executorService.execute(thread1(orderId));
        gracefullyShutdown(executorService);
    }

    @Test
    public void testPessimisticLocking_withMultipleThreads() {
        UUID orderId = saveOrder();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // Get order by id and lock the row for update and update the status to IN_PROGRESS
        var orderStatus = OrderStatus.IN_PROGRESS;
        executorService.execute(updateStatusRequestWithPessimisticLocking(orderId, orderStatus));
        // Get order by id and lock the row for update and update the name to "New_Order_Name"
        var newOrderName = "New_Order_Name";
        executorService.execute(updateNameRequestWithPessimisticLocking(orderId, newOrderName));
        gracefullyShutdown(executorService);
        // assert result
        assertOrderStatusAndName(orderId, orderStatus, newOrderName);
    }

    @Test
    public void testOptimisticLocking_withMultipleThreads() {
        UUID orderId = saveOrder();
        ExecutorService executorService = Executors.newFixedThreadPool(2);
        // Get order by id and lock the row for update and update the status to IN_PROGRESS
        var orderStatus = OrderStatus.IN_PROGRESS;
        executorService.execute(updateStatusRequestWithOptimisticLocking(orderId, orderStatus));
        // Get order by id and lock the row for update and update the name to "New_Order_Name"
        var newOrderName = "New_Order_Name";
        executorService.execute(updateNameRequestWithOptimisticLocking(orderId, newOrderName));
        gracefullyShutdown(executorService);
        // assert result
        assertOrderStatusAndName(orderId, orderStatus, newOrderName);
    }

    private void assertOrderStatusAndName(UUID orderId, OrderStatus orderStatus, String newOrderName) {
        var order = orderRepository.findById(orderId);
        assertEquals(newOrderName, order.getName());
        assertEquals(orderStatus, order.getStatus());
    }

    private void gracefullyShutdown(ExecutorService executorService) {
        executorService.shutdown();
        try {
            if (!executorService.awaitTermination(1000, TimeUnit.MILLISECONDS)) {
                executorService.shutdownNow();
            }
        } catch (InterruptedException e) {
            executorService.shutdownNow();
        }
    }

    @NotNull
    private UUID saveOrder() {
        String orderName = "order001";
        UUID id = UUID.randomUUID();
        Order order = Order.builder().name(orderName).status(OrderStatus.NEW).id(id).build();
        orderRepository.save(order);
        return id;
    }

    @NotNull
    private Runnable thread1(UUID orderId) {
        return () -> transactionTemplate.executeWithoutResult(transactionStatus -> {
            delay(100l);
            log.info("thread1 is starting");
            var orderAfterInsert = orderRepository.findById(orderId);
            log.info("thread1 - orderAfterInsert orderStatus= {}", orderAfterInsert.getStatus());

            orderAfterInsert.setStatus(OrderStatus.IN_PROGRESS);
            orderRepository.update(orderAfterInsert);
            log.info("thread1 is updated");
            var orderAfterUpdate = orderRepository.findById(orderId);
            log.info("thread1 - orderAfterUpdate orderStatus= {}", orderAfterUpdate.getStatus());
            log.info("thread1 is committing");
        });
    }

    @NotNull
    private Runnable thread2(UUID orderId) {
        return () -> transactionTemplate.executeWithoutResult(transactionStatus -> {
            log.info("thread2 is starting");

            var orderAfterInsert = orderRepository.findById(orderId);
            log.info("thread2 - orderAfterInsert orderStatus= {}", orderAfterInsert.getStatus());

            orderAfterInsert.setStatus(OrderStatus.PROCESSED);
            orderRepository.update(orderAfterInsert);
            log.info("thread2 is updated");
            var orderAfterUpdate = orderRepository.findById(orderId);
            log.info("thread2 - orderAfterUpdate orderStatus= {}", orderAfterUpdate.getStatus());
            delay(500l);
            log.info("thread2 is committing");
        });
    }

    @NotNull
    private Runnable updateStatusRequestWithPessimisticLocking(UUID orderId, OrderStatus orderStatus) {

        return () -> transactionTemplate.executeWithoutResult(transactionStatus -> {
            log.info("updateStatusRequest is starting");

            var orderAfterInsert = orderRepository.findByIdForUpdate(orderId);
            delay(150l);
            log.info("updateStatusRequest - orderAfterInsert orderStatus= {}, orderName: {}",
                    orderAfterInsert.getStatus(), orderAfterInsert.getName());

            orderAfterInsert.setStatus(orderStatus);
            orderRepository.update(orderAfterInsert);
            log.info("Status is updated");
            var orderAfterUpdate = orderRepository.findById(orderId);
            log.info("updateStatusRequest - orderAfterUpdate orderStatus= {}, orderName: {}",
                    orderAfterUpdate.getStatus(), orderAfterUpdate.getName());
            log.info("updateStatusRequest is committing");
        });
    }

    @NotNull
    private Runnable updateNameRequestWithPessimisticLocking(UUID orderId, String orderName) {
        delay(100l);
        return () -> transactionTemplate.executeWithoutResult(transactionStatus -> {

            log.info("updateNameRequest is starting");
            var orderAfterInsert = orderRepository.findByIdForUpdate(orderId);
            log.info("updateNameRequest - orderAfterInsert orderStatus= {}, orderName: {}",
                    orderAfterInsert.getStatus(), orderAfterInsert.getName());

            orderAfterInsert.setName(orderName);
            orderRepository.update(orderAfterInsert);
            log.info("Name is updated");
            var orderAfterUpdate = orderRepository.findById(orderId);
            log.info("updateNameRequest - orderAfterUpdate orderStatus= {}, orderName: {}",
                    orderAfterUpdate.getStatus(), orderAfterUpdate.getName());
            log.info("updateNameRequest is committing");
        });
    }

    @NotNull
    private Runnable updateNameRequestWithOptimisticLocking(UUID orderId, String orderName) {
        delay(100l);
        return () -> orderService.updateNameRequestWithOptimisticLocking(orderId, orderName);
    }

    @NotNull
    private Runnable updateStatusRequestWithOptimisticLocking(UUID orderId, OrderStatus orderStatus) {
        return () -> orderService.updateStatusRequestWithOptimisticLocking(orderId, orderStatus);
    }

    private void delay(long delayInMilliseconds) {
        try {
            Thread.sleep(delayInMilliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
