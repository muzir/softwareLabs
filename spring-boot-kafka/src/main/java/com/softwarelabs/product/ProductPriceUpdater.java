package com.softwarelabs.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.security.SecureRandom;
import java.time.Duration;
import java.time.temporal.ChronoUnit;
import java.util.Collections;

@Service
@Slf4j
public class ProductPriceUpdater {

	private final static String TOPIC_NAME = "Product.update";
	@Autowired
	private Producer<String, String> kafkaProducer;
	@Autowired
	private ConsumerFactory consumerFactory;
	@Autowired
	private ObjectMapper mapper;

	@PostConstruct
	public void init() {
		runConsumer();
	}

	public void runConsumer() {
		Consumer<String, String> consumer = consumerFactory.createConsumer();
		consumer.subscribe(Collections.singletonList(TOPIC_NAME));
		int noMessageFound = 0;
		while (true) {
			ConsumerRecords<String, String> consumerRecords = consumer.poll(Duration.of(100, ChronoUnit.MILLIS));
			// 1000 is the time in milliseconds consumer will wait if no record is found at broker.
			if (consumerRecords.count() == 0) {
				noMessageFound++;
				if (noMessageFound > 100)
					// If no message found count is reached to threshold exit loop.
					break;
				else
					continue;
			}
			//print each record.
			consumerRecords.forEach(record -> {
				System.out.println("Record Key " + record.key());
				System.out.println("Record value " + record.value());
				System.out.println("Record partition " + record.partition());
				System.out.println("Record offset " + record.offset());
			});
			// commits the offset of record to broker.
			consumer.commitAsync();
		}
		consumer.close();
	}

	@Scheduled(fixedRate = 60000)
	public void updateProductPrice() throws JsonProcessingException {
		BigDecimal latestProductPrice = getLatestProductPrice();
		ProductPriceChange productPriceChange = new ProductPriceChange("product1", latestProductPrice);
		log.info("Product1 price change to {}", latestProductPrice.toString());
		String productPriceChangeMessage = mapper.writeValueAsString(productPriceChange);
		ProducerRecord<String, String> record = new ProducerRecord<>(TOPIC_NAME, "1", productPriceChangeMessage);
		kafkaProducer.send(record, new ProduceCallback());
	}

	private BigDecimal getLatestProductPrice() {
		SecureRandom sr = new SecureRandom();
		return new BigDecimal(sr.nextInt(100));
	}

	private class ProduceCallback implements Callback {

		@Override public void onCompletion(RecordMetadata metadata, Exception exception) {
			if (exception != null) {
				log.error("Message can't be sent", exception);
				return;
			}
			log.info("Message sent");
			log.info(metadata.toString());
		}
	}
}
