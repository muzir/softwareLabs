package com.softwarelabs.springbootresilience4j.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class RateLimiterConfiguration {
    @Bean
    public GuavaThrottlingService guavaThrottlingService() {
        RateLimiter rateLimiter = RateLimiter.create(1d);
        return new GuavaThrottlingService(rateLimiter);
    }

    @Bean
    public Bucket4jThrottlingService bucket4jThrottlingService() {
        // define the limit 1 times per 1 second
        Bandwidth limit = Bandwidth.simple(1, Duration.ofSeconds(1));
        // construct the bucket
        Bucket bucket = Bucket.builder().addLimit(limit).build();
        return new Bucket4jThrottlingService(bucket);
    }
}
