package com.softwarelabs.spring.kafka.apithrottling.product;

import feign.Headers;
import feign.RequestLine;

@Headers("Content-Type: application/json")
public interface ProductOrderApiClient {

    @RequestLine("POST /product-order")
    String createProductOrder(ProductOrderRequest request);
}
