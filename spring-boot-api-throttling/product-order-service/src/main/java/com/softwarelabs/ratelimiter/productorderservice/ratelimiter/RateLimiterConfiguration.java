package com.softwarelabs.ratelimiter.productorderservice.ratelimiter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfiguration {
    @Bean
    public RateLimiter rateLimiter() {
        return new RateLimiter(1);
    }
}
