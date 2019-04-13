package com.softwarelabs.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softwarelabs.config.BaseIntegrationTest;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class ProductKafkaIntegrationTest extends BaseIntegrationTest {

	@Autowired
	ProductProducer productProducer;

	@Autowired
	ProductConsumer productConsumer;

	@Autowired
	ProductService productService;

	@Test
	public void updateProduct_ifProductChangeEventSent() throws JsonProcessingException, InterruptedException {
		/*Check src->test->resources->db->embedded-postgres-init.sql
		 insert one product to product table which name is product1
		 */
		String productName = "product1";
		BigDecimal newPrice = new BigDecimal("22.25");
		//Sent price change event
		Product productChange = new ProductChange(productName,newPrice);
		productProducer.publishProductChange(productChange);
		Thread.sleep(1000);

		//Product should be updated with new price
		Thread.sleep(1000);

		//Check product is updated
		Product updatedProductParam = new PersistantProduct(productName);
		Product updatedProduct = productService.getProduct(updatedProductParam).get();
		Assert.assertEquals(productName, updatedProduct.name());
		Assert.assertEquals(newPrice, updatedProduct.price());
	}
}
