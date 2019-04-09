package com.softwarelabs.product;

import com.softwarelabs.config.KafkaTopicNames;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.KafkaConsumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.Map;

@Service
@Slf4j
public class ProductConsumer implements EventConsumer<ProductChange> {

	@Autowired
	private ProductService productService;

	@Resource(name = "consumerProps")
	private Map<String, Object> consumerProps;

	@PostConstruct()
	public void init() {
		Thread kafkaConsumerThread = new Thread(() -> {
			KafkaConsumer<String, String> consumer = new KafkaConsumer<>(consumerProps);
			SimpleKafkaConsumer<ProductChange> simpleKafkaConsumer = new SimpleKafkaConsumer<>(KafkaTopicNames.PRODUCT_UPDATE_TOPIC,
					consumer,
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
