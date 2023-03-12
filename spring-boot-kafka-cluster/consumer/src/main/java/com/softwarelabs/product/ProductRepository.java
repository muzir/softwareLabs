package com.softwarelabs.product;

import java.util.Optional;


public interface ProductRepository {
    Optional<Product> findByName(String productName);

    Product save(Product product);

    Product update(Product product);
}
