package com.softwarelabs.product;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.jpa.domain.AbstractPersistable;

import javax.persistence.Entity;

@Getter
@AllArgsConstructor
@NoArgsConstructor
@Entity
public class ProductInDb extends AbstractPersistable<Long> implements Product {
	private String name;
}
