package com.softwarelabs.order;

import com.fasterxml.jackson.core.JsonProcessingException;

import java.util.UUID;

public interface OrderService {
    void updateStatusRequestWithOptimisticLocking(UUID orderId, OrderStatus orderStatus) throws JsonProcessingException;

    void updateNameRequestWithOptimisticLocking(UUID orderId, String orderName) throws JsonProcessingException;
}
