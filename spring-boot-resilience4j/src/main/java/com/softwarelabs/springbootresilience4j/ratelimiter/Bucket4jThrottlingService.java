package com.softwarelabs.springbootresilience4j.ratelimiter;

import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;

import java.time.Clock;
import java.time.Instant;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executors;

@Slf4j
public class Bucket4jThrottlingService implements ThrottlingService {
    private final Bucket bucket;

    public Bucket4jThrottlingService(Bucket bucket) {
        this.bucket = bucket;
    }

    @Override
    public long doSomething() {
        try {
            long startTime = Instant.now(Clock.systemUTC()).toEpochMilli();
            bucket.asScheduler().consume(1, Executors.newScheduledThreadPool(1)).get();
            long endTime = Instant.now(Clock.systemUTC()).toEpochMilli();
            return endTime - startTime;
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return -1;
        }
    }
}
