package com.softwarelabs.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.config.queue.EventState;
import com.softwarelabs.config.queue.QueueEvent;
import com.softwarelabs.config.queue.QueueEventRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

import static com.softwarelabs.config.queue.UpdateOrderCommandQueueEventHandler.UPDATE_ORDER_OPERATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final QueueEventRepository queueEventRepository;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void updateStatusRequestWithOptimisticLocking(UUID orderId, OrderStatus orderStatus)
            throws JsonProcessingException {
        var orderAfterInsert = orderRepository.findById(orderId);
        orderAfterInsert.setStatus(orderStatus);
        try {
            transactionTemplate.executeWithoutResult(transactionStatus -> {
                log.info("updateStatusRequest is starting");
                delay(150l);
                log.info("updateStatusRequest - orderAfterInsert orderStatus= {}, orderName: {}, version: {}",
                        orderAfterInsert.getStatus(), orderAfterInsert.getName(), orderAfterInsert.getVersion());
                orderRepository.updateWithOptimisticLocking(orderAfterInsert);
                log.info("Status is updated");
                var orderAfterUpdate = orderRepository.findById(orderId);
                log.info("updateStatusRequest - orderAfterUpdate orderStatus= {}, orderName: {}, version: {}",
                        orderAfterUpdate.getStatus(), orderAfterUpdate.getName(), orderAfterUpdate.getVersion());
                log.info("updateStatusRequest is committing");
            });
        } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
            insertQueueEvent(orderAfterInsert);
            log.warn("OptimisticLockingFailureException - Update will be retried");
        }
    }

    private void insertQueueEvent(Order orderAfterInsert) throws JsonProcessingException {
        var updateOrderCommand = new UpdateOrderCommand(orderAfterInsert);
        var queueEvent = QueueEvent.builder()
                .id(UUID.randomUUID())
                .classType(UpdateOrderCommand.class.getTypeName())
                .data(objectMapper.writeValueAsString(updateOrderCommand))
                .operation(UPDATE_ORDER_OPERATION)
                .retryCount(0)
                .state(EventState.OPEN)
                .build();
        queueEventRepository.save(queueEvent);
    }

    @Override
    public void updateNameRequestWithOptimisticLocking(UUID orderId, String orderName) throws JsonProcessingException {
        var orderAfterInsert = orderRepository.findById(orderId);
        orderAfterInsert.setName(orderName);
        try {
            transactionTemplate.executeWithoutResult(transactionStatus -> {
                log.info("updateNameRequest is starting");
                log.info("updateNameRequest - orderAfterInsert orderStatus= {}, orderName: {}, version: {}",
                        orderAfterInsert.getStatus(), orderAfterInsert.getName(), orderAfterInsert.getVersion());

                orderRepository.updateWithOptimisticLocking(orderAfterInsert);
                log.info("Name is updated");
                var orderAfterUpdate = orderRepository.findById(orderId);
                log.info("updateNameRequest - orderAfterUpdate orderStatus= {}, orderName: {}, version: {}",
                        orderAfterUpdate.getStatus(), orderAfterUpdate.getName(), orderAfterUpdate.getVersion());
                log.info("updateNameRequest is committing");
            });
        } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
            insertQueueEvent(orderAfterInsert);
            log.warn("OptimisticLockingFailureException - Update will be retried");
        }
    }

    private void delay(long delayInMilliseconds) {
        try {
            Thread.sleep(delayInMilliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
