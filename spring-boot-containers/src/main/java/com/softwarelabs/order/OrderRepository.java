package com.softwarelabs.order;

import java.util.UUID;

public interface OrderRepository {
    Order findById(UUID id);
    Order findByIdForUpdate(UUID id);
    void save(Order order);
    void update(Order order);
}
