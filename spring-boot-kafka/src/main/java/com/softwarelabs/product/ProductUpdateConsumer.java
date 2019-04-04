package com.softwarelabs.product;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

@Service
@Slf4j
public class ProductUpdateConsumer {

	@Autowired
	private ConsumerFactory consumerFactory;

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
		});
		// commits the offset of record to broker.
		consumer.commitAsync();
	}
}
