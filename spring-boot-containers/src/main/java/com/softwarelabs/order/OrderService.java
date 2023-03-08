package com.softwarelabs.order;

import java.util.UUID;

public interface OrderService {
    void updateStatusRequestWithOptimisticLocking(UUID orderId, OrderStatus orderStatus);

    void updateNameRequestWithOptimisticLocking(UUID orderId, String orderName);
}
