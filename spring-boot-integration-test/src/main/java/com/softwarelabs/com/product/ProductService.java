package com.softwarelabs.com.product;

public interface ProductService {

	Product getProduct(Long productId);

	Product createProduct(String name, Long id);
}
