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

import static com.softwarelabs.config.queue.UpdateOrderCommandQueueEventHandler.UPDATE_ORDER_OPERATION;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
public class QueueEventServiceIntTest extends BaseIntegrationTest {

    @Autowired
    QueueEventRepository queueEventRepository;

    @Autowired
    OrderRepository orderRepository;

    @Autowired
    QueueEventService queueEventService;

    @Test
    public void processEventHandlerSuccessfully() throws JsonProcessingException {
        // given
        var orderName = "newOrderName";
        var orderStatus = OrderStatus.NEW;
        var orderId = UUID.randomUUID();

        givenOrder(orderName, orderStatus, orderId);

        var queueEventId = UUID.randomUUID();
        var newOrderName = "newOrderName";
        var newOrderStatus = OrderStatus.IN_PROGRESS;
        var updateOrderCommand = new UpdateOrderCommand(newOrderName, newOrderStatus, orderId);
        QueueEvent queueEvent = givenQueueEvent(queueEventId, updateOrderCommand);

        // when
        queueEventService.process(List.of(queueEvent));

        // then
        var updatedOrder = orderRepository.findById(orderId);
        assertEquals(newOrderStatus, updatedOrder.getStatus());
        assertEquals(newOrderName, updatedOrder.getName());

        var optionalQueueEventActual = queueEventRepository.findById(queueEventId);
        assertTrue(optionalQueueEventActual.isEmpty());

    }

    private QueueEvent givenQueueEvent(UUID queueEventId, UpdateOrderCommand updateOrderCommand)
            throws JsonProcessingException {
        var objectMapper = new ObjectMapper();
        var queueEvent = QueueEvent.builder()
                .id(queueEventId)
                .classType(UpdateOrderCommand.class.getTypeName())
                .data(objectMapper.writeValueAsString(updateOrderCommand))
                .operation(UPDATE_ORDER_OPERATION)
                .retryCount(0)
                .state(EventState.OPEN)
                .build();
        queueEventRepository.save(queueEvent);

        var queueEventActual = queueEventRepository.findById(queueEventId).orElseThrow();
        assertEquals(EventState.OPEN, queueEventActual.getState());
        assertEquals(0, queueEventActual.getRetryCount());
        return queueEvent;
    }

    private void givenOrder(String orderName, OrderStatus orderStatus, UUID orderId) {
        var order = Order.builder().id(orderId).status(orderStatus).name(orderName)
                .build();
        orderRepository.save(order);
    }
}
