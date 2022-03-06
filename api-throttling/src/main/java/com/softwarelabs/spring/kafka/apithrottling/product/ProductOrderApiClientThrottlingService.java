package com.softwarelabs.spring.kafka.apithrottling.product;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class ProductOrderApiClientThrottlingService {

    private final RateLimiter rateLimiter;
    private final ProductOrderApiClient productOrderApiClient;

    public ProductOrderApiClientThrottlingService(RateLimiter rateLimiter,
                                                  ProductOrderApiClient productOrderApiClient) {
        this.rateLimiter = rateLimiter;
        this.productOrderApiClient = productOrderApiClient;
    }

    public void sendProductOrderRequest(ProductOrderRequest request) {
        rateLimiter.acquire();
        productOrderApiClient.send(request);
    }
}
