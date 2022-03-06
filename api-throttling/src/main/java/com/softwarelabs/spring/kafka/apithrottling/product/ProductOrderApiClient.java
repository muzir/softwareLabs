package com.softwarelabs.spring.kafka.apithrottling.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductOrderApiClient {
    void send(ProductOrderRequest request) {
        log.info("ProductOrder processed successfully {}", request.getId());
    }
}
