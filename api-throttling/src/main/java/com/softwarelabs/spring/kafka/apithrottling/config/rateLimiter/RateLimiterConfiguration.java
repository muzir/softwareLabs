package com.softwarelabs.spring.kafka.apithrottling.config.rateLimiter;

import com.google.common.util.concurrent.RateLimiter;
import com.softwarelabs.spring.kafka.apithrottling.product.ProductOrderApiClient;
import com.softwarelabs.spring.kafka.apithrottling.product.ProductOrderApiClientThrottlingService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfiguration {
    @Bean
    public ProductOrderApiClientThrottlingService guavaThrottlingService(ProductOrderApiClient productOrderApiClient) {
        RateLimiter rateLimiter = RateLimiter.create(0.5d);
        return new ProductOrderApiClientThrottlingService(rateLimiter, productOrderApiClient);
    }
}
