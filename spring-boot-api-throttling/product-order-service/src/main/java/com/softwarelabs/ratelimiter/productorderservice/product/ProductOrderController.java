package com.softwarelabs.ratelimiter.productorderservice.product;

import com.softwarelabs.ratelimiter.productorderservice.TooManyRequestsException;
import com.softwarelabs.ratelimiter.productorderservice.ratelimiter.RateLimiter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-order")
@Slf4j
public class ProductOrderController {

    private final RateLimiter rateLimiter;

    public ProductOrderController(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
    }

    @GetMapping
    public String getMessage() {
        return "Product order service running";
    }

    @PostMapping
    public ResponseEntity createProductOrder(@RequestBody ProductOrderRequest productOrderRequest) {
        //log.info("Create product order {}", productOrderRequest);
        if (!rateLimiter.consume(productOrderRequest.toString())) {
            throw new TooManyRequestsException("Too many requests");
        }
        return ResponseEntity.ok().build();
    }
}
