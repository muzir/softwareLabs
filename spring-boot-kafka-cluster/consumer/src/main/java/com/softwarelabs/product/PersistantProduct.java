package com.softwarelabs.product;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersistantProduct implements Product {
    private String name;

    private Long id;

    private BigDecimal price;

    @Override
    public String name() {
        return name;
    }

    @Override
    public Long id() {
        return id;
    }

    @Override
    public BigDecimal price() {
        return price;
    }
}
