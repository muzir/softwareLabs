package com.softwarelabs.springbootresilience4j.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;
import java.util.Random;

@Slf4j
public class GuavaThrottlingService implements ThrottlingService {

    private final RateLimiter rateLimiter;
    private Random random;

    public GuavaThrottlingService(RateLimiter rateLimiter) {
        this.rateLimiter = rateLimiter;
        random = new Random();
    }

    @Override
    public long doSomething() {
        try {
            long startTime = Instant.now(Clock.systemUTC()).toEpochMilli();
            rateLimiter.acquire();
            long sleepTime = random.nextInt(500);
            Thread.sleep(sleepTime);
            long endTime = Instant.now(Clock.systemUTC()).toEpochMilli();
            return endTime - startTime;
        } catch (InterruptedException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
