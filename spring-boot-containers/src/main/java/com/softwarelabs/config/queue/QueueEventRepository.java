package com.softwarelabs.config.queue;

import java.util.Optional;
import java.util.UUID;

public interface QueueEventRepository {
    Optional<QueueEvent> findById(UUID queueEventId);

    void save(QueueEvent queueEvent);
}
