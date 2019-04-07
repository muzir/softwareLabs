package com.softwarelabs.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.config.KafkaTopicNames;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

@Service
@Slf4j
public class ProductConsumer {

	@Autowired
	private ConsumerFactory consumerFactory;

	@Autowired
	private ProductService productService;

	@Autowired
	private ObjectMapper objectMapper;

	private Consumer<String, String> consumer;

	@PostConstruct
	public void init() {
		consumer = consumerFactory.createConsumer();
		consumer.subscribe(Collections.singletonList(KafkaTopicNames.PRODUCT_UPDATE_TOPIC));
	}

	@Scheduled(fixedRate = 5000)
	public void runConsumer() {

		log.info("Polling from broker");
		ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.of(1000, ChronoUnit.MILLIS));
		//print each record.
		consumerRecords.forEach(record -> {
			log.info("Record Key " + record.key());
			log.info("Record value " + record.value());
			log.info("Record partition " + record.partition());
			log.info("Record offset " + record.offset());

			try {
				Product productChange = objectMapper.readValue(record.value(), ProductChange.class);
				Product existingProduct = productService.getProduct(productChange);
				Product newProduct = new PersistantProduct(existingProduct.name(), productChange.price());
				productService.saveProduct(newProduct);
			} catch (IOException e) {
				e.printStackTrace();
			}

		});

		// commits the offset of record to broker.
		consumer.commitAsync();
	}
}
