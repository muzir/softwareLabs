package com.softwarelabs.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;

@Service
@Profile("!integration")
public class ProductProducerSchedular {
	@Autowired
	private final ProductProducer productProducer;

	public ProductProducerSchedular(ProductProducer productProducer) {
		this.productProducer = productProducer;
	}

	@Scheduled(fixedRate = 3000)
	public void run() throws JsonProcessingException {
		productProducer.updateProductPrice(getLatestProductPrice());
	}

	private BigDecimal getLatestProductPrice() {
		SecureRandom sr = new SecureRandom();
		return new BigDecimal(sr.nextInt(100));
	}
}
