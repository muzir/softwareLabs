package com.softwarelabs.product;

import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
public class ProductDao {

	private Map<Long, Product> products = new HashMap();

	public Product getProducts(Long productId) {
		//Product product = new Product(productId, "Product-" + productId);
		Product product = products.get(productId);
		return product;
	}

	public Product createProduct(String name, Long id) {
		Product product = new Product(id, name);
		products.put(id, product);
		return product;
	}
}
