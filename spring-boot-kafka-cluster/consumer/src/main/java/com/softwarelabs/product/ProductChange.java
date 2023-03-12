package com.softwarelabs.product;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class ProductChange implements Product {

    private final String name;
    private final BigDecimal price;
    private final Long id;

    public ProductChange() {
        this.name = "";
        this.id = null;
        this.price = BigDecimal.ZERO;
    }

    public ProductChange(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    @Override
    public Long id() {
        return id;
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
