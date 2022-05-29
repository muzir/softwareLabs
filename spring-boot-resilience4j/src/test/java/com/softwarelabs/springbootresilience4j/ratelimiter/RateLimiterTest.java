package com.softwarelabs.springbootresilience4j.ratelimiter;

import com.google.common.util.concurrent.RateLimiter;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static org.junit.jupiter.api.Assertions.assertTrue;

@Slf4j
public class RateLimiterTest {
    @Test
    void testGuavaRateLimiter() {
        RateLimiter rateLimiter = RateLimiter.create(1d);
        ThrottlingService throttlingService = new GuavaThrottlingService(rateLimiter);
        IntStream.range(0, 10)
                .forEach((i) -> log.info("Call takes {} ms",
                        throttlingService.doSomething()));
    }

    @Test
    void testBucketRateLimiter() {
        Bandwidth limit = Bandwidth.simple(1, Duration.ofSeconds(1));
        Bucket bucket = Bucket.builder().addLimit(limit).build();
        ThrottlingService throttlingService = new Bucket4jThrottlingService(bucket);
        IntStream.range(0, 10)
                .forEach((i) -> log.info("Call takes {} ms",
                        throttlingService.doSomething()));
    }

    @Test
    void testGuavaRateLimiterOneRequestPerTwoSeconds() {
        RateLimiter rateLimiter = RateLimiter.create(0.5d);
        ThrottlingService throttlingService = new GuavaThrottlingService(rateLimiter);
        AtomicInteger counter = new AtomicInteger();
        IntStream.range(0, 10).parallel().forEach(i -> {
                    long result = throttlingService.doSomething();
                    if (1000l >= result) {
                        counter.getAndIncrement();
                        System.out.println("i:" + i + " result:" + result + " counter:" + counter);
                    }
                    assertTrue(counter.get() < 2);
                }
        );
    }
}
