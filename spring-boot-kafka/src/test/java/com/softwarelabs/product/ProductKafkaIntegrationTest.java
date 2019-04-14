package com.softwarelabs.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softwarelabs.kafka.BaseIntegrationTest;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

@RunWith(SpringRunner.class)
@Slf4j
public class ProductKafkaIntegrationTest extends BaseIntegrationTest {

	public static final long WAITING_TIME = 500l;
	@Autowired
	ProductProducer productProducer;
	@Autowired
	ProductService productService;

	@Test
	public void updateProduct_ifProductChangeEventSent() throws JsonProcessingException, InterruptedException {
		/*
		 save new product to product table which name is product1
		 */
		String productName = "product1";
		BigDecimal price = new BigDecimal("22.25");
		Product product = new PersistantProduct(null, productName, price);
		productService.saveProduct(product);

		//Sent price change event
		BigDecimal newPrice = new BigDecimal("20.00");
		Product productChange = new ProductChange(productName, newPrice);
		productProducer.publishProductChange(productChange);

		Thread.sleep(WAITING_TIME);

		//Product should be updated with new price
		Product updatedProductParam = new PersistantProduct(productName);
		Product updatedProduct = productService.getProduct(updatedProductParam).get();
		Assert.assertEquals(productName, updatedProduct.name());
		Assert.assertEquals(newPrice, updatedProduct.price());
	}

	@Test
	public void saveProduct_ifProductChangeEventSent_andProductNotExist() throws JsonProcessingException, InterruptedException {
		String productName = "product2";
		BigDecimal price = new BigDecimal("20.00");
		//Sent price change event
		Product productChange = new ProductChange(productName, price);
		productProducer.publishProductChange(productChange);

		Thread.sleep(WAITING_TIME);

		//Check product is saved
		Product paramSavedProduct = new PersistantProduct(productName);
		Product savedProduct = productService.getProduct(paramSavedProduct).get();
		Assert.assertEquals(productName, savedProduct.name());
		Assert.assertEquals(price, savedProduct.price());
	}
}
