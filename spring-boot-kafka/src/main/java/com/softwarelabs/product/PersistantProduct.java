package com.softwarelabs.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Setter
@Entity
@Table(name = "product")
public class PersistantProduct extends AbstractPersistable<Long> implements Product {

	private String name;
	private BigDecimal price;

	public PersistantProduct() {
	}

	public PersistantProduct(String name, BigDecimal price) {
		this.name = name;
		this.price = price;
	}

	public PersistantProduct(Product product) {
		this.name = product.name();
		this.price = product.price();
	}

	@Override public String name() {
		return name;
	}

	@Override public BigDecimal price() {
		return price;
	}
}
