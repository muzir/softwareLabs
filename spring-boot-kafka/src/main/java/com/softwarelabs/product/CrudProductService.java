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
	public Product getProduct(Product product) {
		Product persistableProduct = productRepository.findByName(product.name()).orElseThrow(
				() -> new RuntimeException(
						"Product is not found by productName:" + product.name()
				)
		);
		return persistableProduct;
	}

	@Override
	public Product createProduct(Product product) {
		PersistantProduct persistantProduct = new PersistantProduct(product);
		return productRepository.save(persistantProduct);
	}
}
