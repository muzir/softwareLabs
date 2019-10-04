package com.softwarelabs.product;

import java.math.BigDecimal;

public interface Product {
	Long id();
	String name();
	BigDecimal price();
	BigDecimal stockPrice();
}
