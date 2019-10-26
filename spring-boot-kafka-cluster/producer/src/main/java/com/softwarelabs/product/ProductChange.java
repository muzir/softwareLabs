package com.softwarelabs.product;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductChange implements Product {

	private final String name;
	private final BigDecimal price;
	private final BigDecimal stockPrice;

	public ProductChange() {
		this.name = "";
		this.price = BigDecimal.ZERO;
		this.stockPrice = BigDecimal.ZERO;
	}

	public ProductChange(String name, BigDecimal price, BigDecimal stockPrice) {
		this.name = name;
		this.price = price;
		this.stockPrice = stockPrice;
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

	@Override
	public BigDecimal stockPrice() {
		return stockPrice;
	}

}
