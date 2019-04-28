package com.softwarelabs.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.kafka.EventConsumer;
import com.softwarelabs.kafka.KafkaConsumerFactory;
import com.softwarelabs.kafka.KafkaConsumerThread;
import com.softwarelabs.kafka.KafkaProducerFactory;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.Resource;
import java.util.Map;

@Configuration
public class ProductConfiguration {

	private final EventConsumer productConsumer;

	@Resource(name = "producerProps")
	private Map<String, Object> producerProps;

	@Resource(name="consumerProps")
	private Map<String, Object> consumerProps;

	@Autowired
	public ProductConfiguration(EventConsumer productConsumer) {
		this.productConsumer = productConsumer;
	}

	@Bean
	public KafkaConsumerFactory<String, String> kafkaConsumerFactory() {
		return new KafkaConsumerFactory<>(consumerProps);
	}

	@Bean
	public Consumer<String, String> kafkaConsumer() {
		return kafkaConsumerFactory().createConsumer(productConsumer.consumerGroupId());
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

	@Bean
	public KafkaProducerFactory<String, String> kafkaProducerFactory() {
		return new KafkaProducerFactory<>(producerProps);
	}

	@Bean
	public Producer<String, String> kafkaProducer() {
		return kafkaProducerFactory().createProducer();
	}

	@Bean
	public ProductProducer productProducer() {
		producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, "producerClientId");
		return new ProductProducer(kafkaProducer(), new ObjectMapper());
	}
}
