package com.softwarelabs.config.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.order.OrderRepository;
import com.softwarelabs.order.UpdateOrderCommand;
import org.springframework.stereotype.Service;

@Service
public class UpdateOrderCommandQueueEventHandler implements QueueEventHandler {

    private final OrderRepository orderRepository;
    public static final String UPDATE_ORDER_OPERATION = "updateOrder";

    public UpdateOrderCommandQueueEventHandler(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    @Override
    public boolean match(QueueEvent queueEvent) {
        if (UpdateOrderCommand.class.getCanonicalName().equals(queueEvent.getClassType()) &&
                UPDATE_ORDER_OPERATION.equals(queueEvent.getOperation())) {
            return true;
        }
        return false;
    }

    @Override
    public void process(QueueEvent queueEvent) {
        try {
            var objectMapper = new ObjectMapper();
            var updateOrderCommand =
                    (UpdateOrderCommand) objectMapper.readValue(queueEvent.getData(),
                            Class.forName(queueEvent.getClassType()));
            orderRepository.update(updateOrderCommand);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
