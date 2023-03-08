package com.softwarelabs.config.queue;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class QueueEvent {
    private final UUID id;
    private final String classType;
    private final String data;
    private final String operation;
    private final int retryCount;
    private final EventState state;
    private final Instant createTime;
    private final Instant updateTime;
}
