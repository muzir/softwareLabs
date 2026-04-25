package com.softwarelabs.product;

import com.softwarelabs.config.BaseIntegrationTest;
import com.softwarelabs.order.Order;
import com.softwarelabs.order.OrderRepository;
import com.softwarelabs.order.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class CrudOrderRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void returnOrderName_ifOrderSavedBefore() {
        String orderName = "order001";
        UUID id = UUID.randomUUID();
        Order order = Order.builder()
                .name(orderName)
                .status(OrderStatus.NEW)
                .id(id).build();
        orderRepository.save(order);
        Order actualOrder = orderRepository.findById(id);
        assertEquals(orderName, actualOrder.getName());
    }

    @Test
    public void returnUpdatedOrderName_ifOrderUpdateWorks() {
        String orderName = "order001";
        UUID id = UUID.randomUUID();

        var order = Order.builder()
                .name(orderName)
                .status(OrderStatus.NEW)
                .id(id).build();
        orderRepository.save(order);
        Order actualOrder = orderRepository.findById(id);
        assertEquals(orderName, actualOrder.getName());

        String newOrderName = "order002";
        order.setName(newOrderName);
        orderRepository.update(order);
        Order updatedOrder = orderRepository.findById(id);
        assertEquals(newOrderName, updatedOrder.getName());
    }
}
