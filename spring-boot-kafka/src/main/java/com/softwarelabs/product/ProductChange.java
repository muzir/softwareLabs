package com.softwarelabs.product;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductChange implements Product {

	private final String name;
	private final BigDecimal price;

	public ProductChange() {
		this.name = "";
		this.price = BigDecimal.ZERO;
	}

	public ProductChange(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

	@Override
	public Long id() {
		return null;
	}

	@Override
	public String name() {
		return name;
	}

	@Override
	public BigDecimal price() {
		return price;
	}

}
