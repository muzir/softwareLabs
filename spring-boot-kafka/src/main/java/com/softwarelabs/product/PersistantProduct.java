package com.softwarelabs.product;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.Random;

@Getter
@Setter
@NoArgsConstructor
public class PersistantProduct implements Product {

    private String name;
    private BigDecimal price;
    private Long id;

    public PersistantProduct(String name) {
        this.id = new Random().nextLong();
        this.name = name;
        this.price = BigDecimal.ZERO;
    }

    public PersistantProduct(Long id, String name, BigDecimal price) {
        this.id = id;
        this.name = name;
        this.price = price;
    }

    public PersistantProduct(Product product) {
        this.id = Optional.ofNullable(product.id()).orElse(new Random().nextLong());
        this.name = product.name();
        this.price = product.price();
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
