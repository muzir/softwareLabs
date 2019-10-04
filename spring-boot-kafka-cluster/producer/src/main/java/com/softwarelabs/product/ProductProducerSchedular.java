package com.softwarelabs.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.javafaker.Faker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Profile("!integration")
public class ProductProducerSchedular {

	private final ProductProducer productProducer;

	@Autowired
	public ProductProducerSchedular(ProductProducer productProducer) {
		this.productProducer = productProducer;
	}

	@Scheduled(fixedRate = 3000)
	public void run() throws JsonProcessingException {
		Faker faker = new Faker();
		Product productChange = new ProductChange(faker.commerce().productName(), new BigDecimal(faker.commerce().price(0, 100)), BigDecimal.ZERO);
		productProducer.publishProductChange(productChange);
	}
}
