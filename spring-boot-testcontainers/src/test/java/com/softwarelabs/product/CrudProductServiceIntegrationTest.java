package com.softwarelabs.product;

import com.softwarelabs.config.BaseIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

@RunWith(SpringRunner.class)
public class CrudProductServiceIntegrationTest extends BaseIntegrationTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	public void returnProductName_ifProductSavedBefore() {
		String productName = "product001";
		PersistableProduct p = new PersistableProduct(productName);
		productRepository.save(p);
		Optional<Product> product = productRepository.findByName(productName);
		Assert.assertTrue(product.isPresent());
		Assert.assertEquals(productName, product.get().name());
	}
}
