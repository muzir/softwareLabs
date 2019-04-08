package com.softwarelabs.product;

import com.softwarelabs.config.KafkaTopicNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class ProductConsumer implements EventConsumer<ProductChange> {

	@Autowired
	private ProductService productService;

	@Autowired
	private ConsumerFactory consumerFactory;

	@PostConstruct
	public void init() {
		Thread kafkaConsumerThread = new Thread(() -> {
			SimpleKafkaConsumer<ProductChange> simpleKafkaConsumer = new SimpleKafkaConsumer<>(KafkaTopicNames.PRODUCT_UPDATE_TOPIC,
					consumerFactory.createConsumer(),
					this);
			simpleKafkaConsumer.readValue();
		});

		/*
		 * Starting the first thread.
		 */
		kafkaConsumerThread.start();

	}

	@Override
	public void consume(ProductChange productChange) {
		Product existingProduct = productService.getProduct(productChange);
		Product newProduct = new PersistantProduct(existingProduct.id(), existingProduct.name(), productChange.price());
		productService.saveProduct(newProduct);

	}

	@Override public Class eventType() {
		return ProductChange.class;
	}
}
