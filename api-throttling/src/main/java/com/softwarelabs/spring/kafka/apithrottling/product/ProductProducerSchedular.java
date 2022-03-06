package com.softwarelabs.spring.kafka.apithrottling.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;
import java.util.UUID;

@Service
@Profile("!integration")
public class ProductProducerSchedular {

    private final ProductProducer productProducer;

    @Autowired
    public ProductProducerSchedular(ProductProducer productProducer) {
        this.productProducer = productProducer;
    }

    @Scheduled(fixedRate = 3000)
    public void run() throws JsonProcessingException {
        Faker faker = new Faker();
        ProductOrderRequest productOrderRequest = new ProductOrderRequest(UUID.randomUUID(), faker.commerce().productName(),
                Instant.now(Clock.systemUTC()), UUID.randomUUID());
        productProducer.publishProductOrderRequest(productOrderRequest);
    }
}
