package com.softwarelabs.product;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;


@Getter
@Setter
@Entity
@Table(name = "product")
public class PersistantProduct extends AbstractPersistable<Long> implements Product {
    private String name;

    public PersistantProduct() {
    }

    public PersistantProduct(String name) {
        this.name = name;
    }

    @Override
    public String name() {
        return name;
    }
}
