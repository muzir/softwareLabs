package com.softwarelabs.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@AllArgsConstructor
@Entity
public class ProductInDb implements Product{
	@Id
	private Long id;
	private String name;
}
