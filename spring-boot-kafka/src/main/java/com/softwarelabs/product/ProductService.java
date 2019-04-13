package com.softwarelabs.product;

import java.util.Optional;

public interface ProductService {

	Optional<Product> getProduct(Product product);

	Product saveProduct(Product product);
}
