package com.softwarelabs.config.queue.order;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.config.queue.QueueEvent;
import com.softwarelabs.config.queue.QueueEventHandler;
import com.softwarelabs.order.OrderService;
import com.softwarelabs.order.command.UpdateOrderStatusCommand;
import org.springframework.stereotype.Service;

@Service
public class UpdateOrderStatusCommandQueueEventHandler implements QueueEventHandler {

    private final OrderService orderService;
    private final ObjectMapper objectMapper;

    public UpdateOrderStatusCommandQueueEventHandler(
            OrderService orderService,
            ObjectMapper objectMapper) {
        this.orderService = orderService;
        this.objectMapper = objectMapper;
    }

    @Override
    public boolean match(QueueEvent queueEvent) {
        if (UpdateOrderStatusCommand.class.getCanonicalName().equals(queueEvent.getClassType()) &&
                UPDATE_ORDER_STATUS_OPERATION.equals(queueEvent.getOperation())) {
            return true;
        }
        return false;
    }

    @Override
    public void process(QueueEvent queueEvent) {
        try {
            var updateOrderStatusCommand =
                    (UpdateOrderStatusCommand) objectMapper.readValue(queueEvent.getData(),
                            Class.forName(queueEvent.getClassType()));
            orderService.updateStatusRequestWithOptimisticLocking(updateOrderStatusCommand);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
