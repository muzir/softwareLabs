package com.softwarelabs.spring.kafka.apithrottling.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@Profile("!integration")
@Slf4j
public class ProductProducerScheduler {

    private final ProductProducer productProducer;

    @Autowired
    public ProductProducerScheduler(ProductProducer productProducer) {
        this.productProducer = productProducer;
    }

    @Scheduled(initialDelay = 30000, fixedDelay = 3_000_000)
    public void run() {
        publishProductOrderRequests();
    }

    private void publishProductOrderRequests() {
        IntStream.range(0, 200).forEach(i -> {
            Faker faker = new Faker();
            ProductOrderRequest productOrderRequest =
                    new ProductOrderRequest(UUID.randomUUID(), faker.commerce().productName(),
                            Instant.now(Clock.systemUTC()), UUID.randomUUID());
            try {
                productProducer.publishProductOrderRequest(productOrderRequest);
            } catch (JsonProcessingException e) {
                log.error("Product order request publishing has failed", e);
            }
        });
    }
}
