package com.softwarelabs.product;

import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository {
    Optional<Product> findByName(String productName);

    Product save(Product product);
}
