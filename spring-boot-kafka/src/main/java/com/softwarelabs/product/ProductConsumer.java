package com.softwarelabs.product;

import com.softwarelabs.config.KafkaTopicNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductConsumer implements EventConsumer<ProductChange> {

	@Autowired
	private ProductService productService;

	@Override
	public void consume(ProductChange productChange) {
		log.info("Consume productChange {} {}", productChange.name(), productChange.price());
		Product product = new PersistantProduct(productChange);
		productService.getProduct(product)
				.map(p -> productService.saveProduct(new PersistantProduct(p.id(), productChange.name(), productChange.price())))
				.orElse(productService.saveProduct(productChange));
	}

	@Override public Class eventType() {
		return ProductChange.class;
	}

	@Override public String topicName() {
		return KafkaTopicNames.PRODUCT_UPDATE_TOPIC;
	}
}
