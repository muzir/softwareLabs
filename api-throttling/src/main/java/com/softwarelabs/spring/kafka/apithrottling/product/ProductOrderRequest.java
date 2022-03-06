package com.softwarelabs.spring.kafka.apithrottling.product;

import lombok.AllArgsConstructor;
import lombok.Value;

import java.time.Instant;
import java.util.UUID;

@Value
@AllArgsConstructor
public class ProductOrderRequest {
    private final UUID id;
    private final String productName;
    private final Instant productOrderCreationTime;
    private final UUID orderId;
}
