package com.softwarelabs.product;

import lombok.Getter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "product")
public class PersistantProduct extends AbstractPersistable<Long> implements Product {

	private String name;
	private BigDecimal price;

	public PersistantProduct() {
	}

	public PersistantProduct(String name) {
		this.name = name;
		this.price = BigDecimal.ZERO;
	}

	public PersistantProduct(Long id, String name, BigDecimal price) {
		this.setId(id);
		this.name = name;
		this.price = price;
	}

	public PersistantProduct(Product product) {
		this.setId(product.id());
		this.name = product.name();
		this.price = product.price();
	}

	@Override public Long id() {
		return getId();
	}

	@Override public String name() {
		return name;
	}

	@Override public BigDecimal price() {
		return price;
	}
}
