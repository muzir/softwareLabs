package com.softwarelabs.product;

public interface ProductService {

	Product getProduct(String productName);

	Product createProduct(String name);
}
