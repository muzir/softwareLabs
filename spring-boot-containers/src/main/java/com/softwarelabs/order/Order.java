package com.softwarelabs.order;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class Order {
    private String name;
    private OrderStatus status;
    private UUID id;
    private Instant createTime;
    private Instant updateTime;
}
