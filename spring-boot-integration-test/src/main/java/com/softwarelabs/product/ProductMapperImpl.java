package com.softwarelabs.product;

import org.springframework.stereotype.Component;

@Component
public class ProductMapperImpl implements ProductMapper {
	@Override
	public ProductDto mapToProductDto(Product product) {
		ProductDto productDto = new ProductDto(product.getName());
		return productDto;
	}
}
