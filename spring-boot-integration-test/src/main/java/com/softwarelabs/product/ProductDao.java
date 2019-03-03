package com.softwarelabs.product;

import org.springframework.stereotype.Service;

@Service
public class ProductDao {

	public Product getProducts(Long productId) {
		Product product = new Product(productId, "Product-" + productId);
		return product;
	}

	public Product createProduct(String name, Long id) {
		Product product = new Product(id, name);
		return product;
	}
}
