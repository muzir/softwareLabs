package com.softwarelabs.product;

import com.softwarelabs.config.BaseIntegrationTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
public class CrudProductServiceIntegrationTest extends BaseIntegrationTest {

	@Autowired
	private ProductRepository productRepository;

	@Test
	public void test() {
		PersistableProduct p = new PersistableProduct("name");
		productRepository.save(p);
	}
}
