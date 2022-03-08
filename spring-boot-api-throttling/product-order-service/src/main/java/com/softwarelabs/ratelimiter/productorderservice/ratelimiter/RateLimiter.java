package com.softwarelabs.ratelimiter.productorderservice.ratelimiter;

import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import java.time.Duration;

public class RateLimiter {

    private static final int DEFAULT_REQUESTS_PER_SECONDS = 1;
    private Bucket bucket;
    private final int requestsPerSeconds;

    public RateLimiter() {
        this(DEFAULT_REQUESTS_PER_SECONDS);
    }

    public RateLimiter(int requestsPerSecond) {
        this.requestsPerSeconds = requestsPerSecond;
        this.bucket = newBucket();
    }

    public boolean consume() {
        return bucket.tryConsume(1);
    }

    private Bucket newBucket() {
        return Bucket4j.builder()
                .addLimit(Bandwidth.classic(requestsPerSeconds,
                        Refill.intervally(requestsPerSeconds, Duration.ofSeconds(1))))
                .build();
    }
}
