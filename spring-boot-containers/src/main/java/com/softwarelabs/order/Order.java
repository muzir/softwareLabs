package com.softwarelabs.order;

import lombok.Data;

import java.time.Instant;
import java.util.UUID;

@Data
public class Order {
    private String name;
    private OrderStatus status;
    private UUID id;
    private Instant createTime;
    private Instant updateTime;
}
