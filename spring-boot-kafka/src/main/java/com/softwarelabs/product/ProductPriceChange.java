package com.softwarelabs.product;

import lombok.Getter;

import java.io.Serializable;
import java.math.BigDecimal;

@Getter
public class ProductPriceChange implements Product, Serializable {

	private final String name;
	private final BigDecimal price;

	public ProductPriceChange(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

	@Override public String name() {
		return name;
	}

	@Override public BigDecimal price() {
		return price;
	}


}
