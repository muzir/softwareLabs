package com.softwarelabs.config.queue;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.config.BaseIntegrationTest;
import com.softwarelabs.order.Order;
import com.softwarelabs.order.OrderRepository;
import com.softwarelabs.order.OrderStatus;
import com.softwarelabs.order.UpdateOrderCommand;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
public class QueueEventServiceIntTest extends BaseIntegrationTest {

    @Autowired
    QueueEventRepository queueEventRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    QueueEventService queueEventService;

    @Test
    public void processEventHandlerSuccessfully() throws JsonProcessingException, ClassNotFoundException {
        var orderName = "newOrderName";
        var orderStatus = OrderStatus.NEW;
        var queueEventId = UUID.randomUUID();
        var orderId = UUID.randomUUID();

        givenOrder(orderName, orderStatus, orderId);


        var newOrderName = "newOrderName";
        var updateOrderCommand = new UpdateOrderCommand(newOrderName, orderStatus, orderId);
        var objectMapper = new ObjectMapper();
        var queueEvent = QueueEvent.builder()
                .id(queueEventId)
                .classType(UpdateOrderCommand.class.getTypeName())
                .data(objectMapper.writeValueAsString(updateOrderCommand))
                .operation("updateOrder")
                .retryCount(0)
                .state(EventState.OPEN)
                .build();
        queueEventRepository.save(queueEvent);

        queueEventService.process(List.of(queueEvent));

        var queueEventActual = queueEventRepository.findById(queueEventId).orElseThrow();
        assertEquals(EventState.DONE, queueEventActual.getState());
        assertEquals(1, queueEventActual.getRetryCount());
    }

    private void givenOrder(String orderName, OrderStatus orderStatus, UUID orderId) {
        var order = Order.builder().id(orderId).status(orderStatus).name(orderName)
                .build();
        orderRepository.save(order);
    }
}
