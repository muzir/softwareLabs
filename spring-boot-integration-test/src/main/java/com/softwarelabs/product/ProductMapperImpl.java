package com.softwarelabs.product;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class ProductMapperImpl implements ProductMapper {
    @Override
    public ProductDto mapToProductDto(Product product) {
        String productName = Optional.ofNullable(product).map(Product::getName).orElse(null);
        return new ProductDto(productName);
    }
}
