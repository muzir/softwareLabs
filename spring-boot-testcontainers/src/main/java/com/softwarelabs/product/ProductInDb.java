package com.softwarelabs.product;

import lombok.AllArgsConstructor;
import lombok.Getter;

import javax.persistence.Entity;

@Getter
@AllArgsConstructor
@Entity
public class ProductInDb implements Product{
	private Long id;
	private String name;
}
