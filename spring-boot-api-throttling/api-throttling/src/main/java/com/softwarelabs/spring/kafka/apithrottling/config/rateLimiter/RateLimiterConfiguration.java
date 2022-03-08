package com.softwarelabs.spring.kafka.apithrottling.config.rateLimiter;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.common.util.concurrent.RateLimiter;
import com.softwarelabs.spring.kafka.apithrottling.config.NoOpHostnameVerifier;
import com.softwarelabs.spring.kafka.apithrottling.config.NotFoundDecoder;
import com.softwarelabs.spring.kafka.apithrottling.product.ProductOrderApiClient;
import com.softwarelabs.spring.kafka.apithrottling.product.ProductOrderApiClientThrottlingService;
import feign.Client;
import feign.Feign;
import feign.jackson.JacksonDecoder;
import feign.jackson.JacksonEncoder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RateLimiterConfiguration {

    @Value("${product_order_service_base_url}")
    private String baseUrl;

    public static final ObjectMapper mapper =
            new ObjectMapper()
                    .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
                    .configure(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS, false)
                    .setSerializationInclusion(JsonInclude.Include.NON_NULL)
                    .registerModule(new JavaTimeModule());

    @Bean
    public ProductOrderApiClientThrottlingService guavaThrottlingService(ProductOrderApiClient productOrderApiClient) {
        RateLimiter rateLimiter = RateLimiter.create(10d);
        return new ProductOrderApiClientThrottlingService(rateLimiter, productOrderApiClient);
    }

    @Bean
    ProductOrderApiClient apiClient() {
        final var builder =
                Feign.builder()
                        .client(new Client.Default(null, NoOpHostnameVerifier.INSTANCE))
                        .encoder(new JacksonEncoder(mapper))
                        .decoder(new NotFoundDecoder(new JacksonDecoder(mapper)))
                        .decode404();

        return builder.target(ProductOrderApiClient.class, baseUrl);
    }
}
