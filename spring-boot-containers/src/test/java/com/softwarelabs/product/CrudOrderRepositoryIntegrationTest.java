package com.softwarelabs.product;

import com.softwarelabs.config.BaseIntegrationTest;
import com.softwarelabs.order.Order;
import com.softwarelabs.order.OrderRepository;
import com.softwarelabs.order.OrderStatus;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.UUID;

@RunWith(SpringRunner.class)
public class CrudOrderRepositoryIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private OrderRepository orderRepository;

    @Test
    public void returnOrderName_ifOrderSavedBefore() {
        String orderName = "order001";
        Order order = new Order();
        order.setName(orderName);
        order.setStatus(OrderStatus.NEW);
        UUID id = UUID.randomUUID();
        order.setId(id);
        orderRepository.save(order);
        Order actualOrder = orderRepository.findById(id);
        Assert.assertEquals(orderName, actualOrder.getName());
    }

    @Test
    public void returnUpdatedOrderName_ifOrderUpdateWorks() {
        String orderName = "order001";

        Order order = new Order();
        order.setName(orderName);
        order.setStatus(OrderStatus.NEW);
        UUID id = UUID.randomUUID();
        order.setId(id);
        orderRepository.save(order);
        Order actualOrder = orderRepository.findById(id);
        Assert.assertEquals(orderName, actualOrder.getName());

        String newOrderName = "order002";
        order.setName(newOrderName);
        orderRepository.update(order);
        Order updatedOrder = orderRepository.findById(id);
        Assert.assertEquals(newOrderName, updatedOrder.getName());
    }
}
