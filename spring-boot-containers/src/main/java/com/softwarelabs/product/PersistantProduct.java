package com.softwarelabs.product;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersistantProduct implements Product {
    private String name;

    private Long id;

    @Override
    public String name() {
        return name;
    }

    @Override
    public Long id() {
        return id;
    }
}
