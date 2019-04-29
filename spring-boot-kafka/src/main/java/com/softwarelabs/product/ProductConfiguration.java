package com.softwarelabs.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.kafka.EventConsumer;
import com.softwarelabs.kafka.KafkaConsumerFactory;
import com.softwarelabs.kafka.KafkaConsumerThread;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ProductConfiguration {

	private final EventConsumer productConsumer;
	private final KafkaConsumerFactory<String, String> kafkaConsumerFactory;

	@Autowired
	public ProductConfiguration(EventConsumer productConsumer, KafkaConsumerFactory kafkaConsumerFactory
	) {
		this.productConsumer = productConsumer;
		this.kafkaConsumerFactory = kafkaConsumerFactory;
	}

	@Bean
	public Consumer<String, String> kafkaConsumer() {
		return kafkaConsumerFactory.createConsumer(productConsumer.consumerGroupId());
	}

	@Bean
	public KafkaConsumerThread productConsumerThread() {
		KafkaConsumerThread<ProductChange, String, String> productConsumerThread =
				new KafkaConsumerThread(productConsumer, kafkaConsumer(), new ObjectMapper());
		Thread consumer = new Thread(() -> {
			productConsumerThread.run();
		});
		/*
		 * Starting the thread.
		 */
		consumer.start();
		return productConsumerThread;
	}
}
