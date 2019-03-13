package com.softwarelabs.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class CrudProductService implements ProductService {

	private final ProductRepository productRepository;

	@Autowired
	public CrudProductService(ProductRepository productRepository) {
		this.productRepository = productRepository;
	}

	@Override
	public Product getProduct(String productName) {
		PersistableProduct persistableProduct = productRepository.findByName(productName).orElseThrow(
				() -> new RuntimeException(
						"Product is not found by productName:" + productName
				)
		);
		return persistableProduct;
	}

	@Override
	public PersistableProduct createProduct(String name) {
		PersistableProduct product = new PersistableProduct(name);
		return productRepository.save(product);
	}
}
