package com.softwarelabs.springbootresilience4j.payment;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.feign.FeignDecorators;
import io.github.resilience4j.feign.Resilience4jFeign;
import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
@Slf4j
public class ApiClientConfiguration {

    @Value("${base_url}")
    private String baseUrl;

    public static final ObjectMapper mapper =
            new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL).registerModule(new JavaTimeModule());

    @Bean
    PaymentApiClient apiClient() {
        CircuitBreaker circuitBreaker = CircuitBreaker.ofDefaults("backendName");
        RateLimiterConfig config =
                RateLimiterConfig.custom()
                        .limitRefreshPeriod(Duration.ofSeconds(2))
                        .limitForPeriod(1)
                        .timeoutDuration(Duration.ofMinutes(10))
                        .build();

        // Create registry
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.of(config);

        // Use registry
        RateLimiter rateLimiterWithDefaultConfig = rateLimiterRegistry.rateLimiter("name1");

        rateLimiterWithDefaultConfig.getEventPublisher()
                .onEvent(onEventConsumer -> log.info("onEventConsumer {}", onEventConsumer));

        FeignDecorators decorators = FeignDecorators.builder().withCircuitBreaker(circuitBreaker)
                .withRateLimiter(rateLimiterWithDefaultConfig).build();
        PaymentApiClient apiClient = Resilience4jFeign.builder(decorators).encoder(new JacksonEncoder(mapper))
                .decoder(new NotFoundDecoder(new JacksonDecoder(mapper))).target(PaymentApiClient.class, baseUrl);
        return apiClient;
    }
}
