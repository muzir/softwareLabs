package com.softwarelabs.product;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
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

	@PostConstruct
	public void init() {
		runConsumer();
	}

	public void runConsumer() {
		Consumer<String, String> consumer = consumerFactory.createConsumer();
		consumer.subscribe(Collections.singletonList(KafkaTopicNames.PRODUCT_UPDATE_TOPIC));
		int noMessageFound = 0;
		while (true) {
			//log.info("Polling from broker");
			ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.of(10000, ChronoUnit.MILLIS));
			// 1000 is the time in milliseconds consumer will wait if no record is found at broker.
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
}
