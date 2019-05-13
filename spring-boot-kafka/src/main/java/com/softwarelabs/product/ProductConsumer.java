package com.softwarelabs.product;

import com.softwarelabs.kafka.EventConsumer;
import com.softwarelabs.kafka.KafkaTopicNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductConsumer implements EventConsumer<ProductChange> {

	private final ProductService productService;

	@Autowired
	public ProductConsumer(ProductService productService) {
		this.productService = productService;
	}

	@Override
	public void consume(ProductChange productChange) {
		log.info("Consume productChange name: {}  price: {}", productChange.name(), productChange.price());
		Product product = new PersistantProduct(productChange);
		productService.getProduct(product)
				.map(p -> {
							log.info("Product {} is exist", product.name());
							return productService.saveProduct(new PersistantProduct(p.id(), productChange.name(), productChange.price()));

						}
				)
				.orElseGet(() -> {
							log.info("Product {} is not exist", product.name());
							return productService.saveProduct(productChange);
						}

				);
	}

	@Override
	public Class eventType() {
		return ProductChange.class;
	}

	@Override
	public String topicName() {
		return KafkaTopicNames.PRODUCT_CHANGE_TOPIC;
	}

	@Override
	public String consumerGroupId() {
		return "consumerGroup1";
	}
}
