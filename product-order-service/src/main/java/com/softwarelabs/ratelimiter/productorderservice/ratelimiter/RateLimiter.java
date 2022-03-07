package com.softwarelabs.springbootresilience4j.ratelimiter;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;
import io.github.bucket4j.Bandwidth;
import io.github.bucket4j.Bucket;
import io.github.bucket4j.Bucket4j;
import io.github.bucket4j.Refill;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class RateLimiter {

    static final int DEFAULT_REQUESTS_PER_SECONDS = 1;

    private static final int MAX_CACHE_SIZE = 1000;
    private static final int MAX_CACHE_IDLE_TIME_MINUTES = 5;

    private final LoadingCache<String, Bucket> cache = initCache();
    private final int requestsPerSeconds;

    public RateLimiter() {
        this(DEFAULT_REQUESTS_PER_SECONDS);
    }

    public RateLimiter(int requestsPerSecond) {
        this.requestsPerSeconds = requestsPerSecond;
    }

    public boolean consume(String key) {
        final var bucket = cache.getUnchecked(key);
        return bucket.tryConsume(1);
    }

    private LoadingCache<String, Bucket> initCache() {
        return CacheBuilder
                .newBuilder()
                .maximumSize(MAX_CACHE_SIZE)
                .expireAfterAccess(MAX_CACHE_IDLE_TIME_MINUTES, TimeUnit.MINUTES)
                .build(CacheLoader.from(this::newBucket));
    }

    private Bucket newBucket() {
        return Bucket4j.builder()
                .addLimit(Bandwidth.classic(requestsPerSeconds,
                        Refill.intervally(requestsPerSeconds, Duration.ofSeconds(1))))
                .build();
    }
}
