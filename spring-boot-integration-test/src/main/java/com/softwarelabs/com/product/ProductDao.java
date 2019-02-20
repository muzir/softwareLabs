package com.softwarelabs.com.product;

import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ProductDao {

  public Product getProducts(Long productId) {
    Long randomProductId = new SecureRandom().nextLong();
    Product product = new Product();
    product.setId(randomProductId);
    product.setName("Product-" + randomProductId);
    return product;
  }

  public Product createProduct(String name, Long id) {
    Product product = new Product();
    product.setName(name);
    product.setId(id);
    return product;
  }
}
