package com.softwarelabs.order;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final TransactionTemplate transactionTemplate;

    @Override
    public void updateStatusRequestWithOptimisticLocking(UUID orderId, OrderStatus orderStatus) {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            log.info("updateStatusRequest is starting");

            var orderAfterInsert = orderRepository.findById(orderId);
            delay(150l);
            log.info("updateStatusRequest - orderAfterInsert orderStatus= {}, orderName: {}",
                    orderAfterInsert.getStatus(), orderAfterInsert.getName());

            orderAfterInsert.setStatus(orderStatus);
            orderRepository.updateWithOptimisticLocking(orderAfterInsert);
            log.info("Status is updated");
            var orderAfterUpdate = orderRepository.findById(orderId);
            log.info("updateStatusRequest - orderAfterUpdate orderStatus= {}, orderName: {}",
                    orderAfterUpdate.getStatus(), orderAfterUpdate.getName());
            log.info("updateStatusRequest is committing");
        });
    }

    @Override
    public void updateNameRequestWithOptimisticLocking(UUID orderId, String orderName) {
        transactionTemplate.executeWithoutResult(transactionStatus -> {
            log.info("updateNameRequest is starting");
            var orderAfterInsert = orderRepository.findById(orderId);
            log.info("updateNameRequest - orderAfterInsert orderStatus= {}, orderName: {}",
                    orderAfterInsert.getStatus(), orderAfterInsert.getName());

            orderAfterInsert.setName(orderName);
            orderRepository.updateWithOptimisticLocking(orderAfterInsert);
            log.info("Name is updated");
            var orderAfterUpdate = orderRepository.findById(orderId);
            log.info("updateNameRequest - orderAfterUpdate orderStatus= {}, orderName: {}",
                    orderAfterUpdate.getStatus(), orderAfterUpdate.getName());
            log.info("updateNameRequest is committing");
        });
    }

    private void delay(long delayInMilliseconds) {
        try {
            Thread.sleep(delayInMilliseconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
