package com.softwarelabs.com.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

	private final ProductDao productDao;

	public ProductServiceImpl(ProductDao productDao) {
		this.productDao = productDao;
	}

	@Override
	public Product getProduct(Long productId) {
		return productDao.getProducts(productId);
	}

	@Override
	public Product createProduct(String name, Long id) {
		return productDao.createProduct(name, id);
	}
}
