package com.softwarelabs.product;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;
import javax.persistence.Table;

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

	@Override public String name() {
		return name;
	}
}
