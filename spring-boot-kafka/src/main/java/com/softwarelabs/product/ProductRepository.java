package com.softwarelabs.product;

import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.Optional;

@Repository
public interface ProductRepository {
    Optional<Product> findByName(String productName);

    Product save(Product product);

    void updateProductPrice(String productName, BigDecimal price);
}
