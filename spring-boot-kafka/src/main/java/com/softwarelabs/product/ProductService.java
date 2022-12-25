package com.softwarelabs.product;

import java.math.BigDecimal;
import java.util.Optional;

public interface ProductService {

    Optional<Product> getProduct(Product product);

    Product saveProduct(Product product);

    void updateProductPrice(String productName, BigDecimal productPrice);
}
