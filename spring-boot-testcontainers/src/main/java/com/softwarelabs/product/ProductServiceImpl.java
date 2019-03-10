package com.softwarelabs.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

	private final ProductDao productDao;

	@Autowired
	public ProductServiceImpl(ProductDao productDao) {
		this.productDao = productDao;
	}

	@Override
	public Product getProduct(String productName) {
		ProductInDb productInDb = productDao.findByName(productName).orElseThrow(
				() -> new RuntimeException(
						"Product is not found by productName:" + productName
				)
		);
		return productInDb;
	}

	@Override
	public ProductInDb createProduct(String name) {
		ProductInDb product = new ProductInDb(name);
		return productDao.save(product);
	}
}
