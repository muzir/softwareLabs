package com.softwarelabs.product;

import org.mockito.Mockito;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@TestConfiguration
public class TestConfig {
    @Bean
    @Primary
    public ProductService productService() {
        return Mockito.mock(ProductService.class);
    }

    @Bean
    @Primary
    public ProductMapper productMapper() {
        return Mockito.mock(ProductMapper.class);
    }
}
