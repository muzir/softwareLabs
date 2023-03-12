package com.softwarelabs.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.config.queue.EventState;
import com.softwarelabs.config.queue.QueueEvent;
import com.softwarelabs.config.queue.QueueEventRepository;
import com.softwarelabs.order.command.UpdateOrderNameCommand;
import com.softwarelabs.order.command.UpdateOrderStatusCommand;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;

import java.util.UUID;

import static com.softwarelabs.config.queue.order.UpdateOrderNameCommandQueueEventHandler.UPDATE_ORDER_NAME_OPERATION;
import static com.softwarelabs.config.queue.order.UpdateOrderNameCommandQueueEventHandler.UPDATE_ORDER_STATUS_OPERATION;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final ObjectMapper objectMapper;
    private final QueueEventRepository queueEventRepository;

    @Override
    public void updateStatusRequestWithOptimisticLocking(UpdateOrderStatusCommand updateOrderStatusCommand)
            throws JsonProcessingException {
        try {
            UUID orderId = updateOrderStatusCommand.getId();
            var orderAfterInsert = orderRepository.findById(orderId);
            orderAfterInsert.setStatus(updateOrderStatusCommand.getStatus());
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
        } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
            insertQueueEvent(updateOrderStatusCommand);
            log.warn("OptimisticLockingFailureException - Update will be retried");
        }
    }

    private void insertQueueEvent(UpdateOrderStatusCommand updateOrderStatusCommand)
            throws JsonProcessingException {
        var queueEvent =
                QueueEvent.builder().id(UUID.randomUUID()).classType(UpdateOrderStatusCommand.class.getTypeName())
                        .data(objectMapper.writeValueAsString(updateOrderStatusCommand))
                        .operation(UPDATE_ORDER_STATUS_OPERATION)
                        .retryCount(0).state(EventState.OPEN).build();
        queueEventRepository.save(queueEvent);
    }

    private void insertQueueEvent(UpdateOrderNameCommand updateOrderNameCommand)
            throws JsonProcessingException {
        var queueEvent =
                QueueEvent.builder().id(UUID.randomUUID()).classType(UpdateOrderStatusCommand.class.getTypeName())
                        .data(objectMapper.writeValueAsString(updateOrderNameCommand))
                        .operation(UPDATE_ORDER_NAME_OPERATION)
                        .retryCount(0).state(EventState.OPEN).build();
        queueEventRepository.save(queueEvent);
    }

    @Override
    public void updateNameRequestWithOptimisticLocking(UpdateOrderNameCommand updateOrderNameCommand)
            throws JsonProcessingException {
        try {
            UUID orderId = updateOrderNameCommand.getId();
            var orderAfterInsert = orderRepository.findById(orderId);
            orderAfterInsert.setName(updateOrderNameCommand.getOrderName());
            log.info("updateNameRequest is starting");
            log.info("updateNameRequest - orderAfterInsert orderStatus= {}, orderName: {}, version: {}",
                    orderAfterInsert.getStatus(), orderAfterInsert.getName(), orderAfterInsert.getVersion());

            orderRepository.updateWithOptimisticLocking(orderAfterInsert);
            log.info("Name is updated");
            var orderAfterUpdate = orderRepository.findById(orderId);
            log.info("updateNameRequest - orderAfterUpdate orderStatus= {}, orderName: {}, version: {}",
                    orderAfterUpdate.getStatus(), orderAfterUpdate.getName(), orderAfterUpdate.getVersion());
            log.info("updateNameRequest is committing");
        } catch (OptimisticLockingFailureException optimisticLockingFailureException) {
            insertQueueEvent(updateOrderNameCommand);
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
