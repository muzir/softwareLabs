package com.softwarelabs.order;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface OrderRepository {
    Order findById(UUID id);

    Order findByIdForUpdate(UUID id);

    void save(Order order);

    void saveBulk(List<Order> orders);

    void update(Order order);

    void updateWithOptimisticLocking(Order order);

    Order findTopCase(Timestamp createdAt);
}
