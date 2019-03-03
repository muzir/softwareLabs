package com.softwarelabs.product;

public interface ProductService {

	Product getProduct(Long productId);

	Product createProduct(String name, Long id);
}
