package com.softwarelabs.product;

import java.util.Optional;

public interface ProductService {

	Optional<Product> getProductByName(String productName);

	Product saveProduct(Product product);
}
