package com.softwarelabs.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.kafka.KafkaConsumerFactory;
import com.softwarelabs.kafka.KafkaConsumerThread;
import org.apache.kafka.clients.consumer.Consumer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;

@Configuration
public class ProductConfiguration {

	private final ProductConsumer productConsumer;

	@Resource(name = "consumerProps")
	private Map<String, Object> consumerProps;

	@Autowired
	public ProductConfiguration(ProductConsumer productConsumer) {
		this.productConsumer = productConsumer;
	}

	@Bean
	public KafkaConsumerFactory<String, String> kafkaConsumerFactory() {
		return new KafkaConsumerFactory<>(consumerProps);
	}

	@Bean
	public Consumer<String, String> kafkaConsumer() {
		return kafkaConsumerFactory().createConsumer();
	}

	@Bean
	public KafkaConsumerThread kafkaConsumerThread() {
		KafkaConsumerThread<ProductChange, String, String> kafkaConsumerThread =
				new KafkaConsumerThread(productConsumer, kafkaConsumer(), new ObjectMapper());
		Thread consumer = new Thread(() -> {
			kafkaConsumerThread.run();
		});
		/*
		 * Starting the thread.
		 */
		consumer.start();
		return kafkaConsumerThread;
	}
}
