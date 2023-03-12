package com.softwarelabs.order;

import lombok.Builder;
import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
@Builder
public class Order {
    private UUID id;
    private String name;
    private OrderStatus status;
    private int version;
    private Instant createTime;
    private Instant updateTime;
}
