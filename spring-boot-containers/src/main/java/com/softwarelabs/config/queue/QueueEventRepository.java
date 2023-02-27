package com.softwarelabs.config.queue;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface QueueEventRepository {
    Optional<QueueEvent> findById(UUID queueEventId);

    List<QueueEvent> findAll();

    void save(QueueEvent queueEvent);

    void delete(UUID id);
}
