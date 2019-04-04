package com.softwarelabs.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.security.SecureRandom;

@Service
@Slf4j
public class ProductUpdateProducer {

	@Autowired
	private Producer<String, String> kafkaProducer;

	@Autowired
	private ObjectMapper mapper;

	@Scheduled(fixedRate = 10000)
	public void updateProductPrice() throws JsonProcessingException {
		BigDecimal latestProductPrice = getLatestProductPrice();
		ProductPriceChange productPriceChange = new ProductPriceChange("product1", latestProductPrice);
		log.info("Product1 price change to {}", latestProductPrice.toString());
		String productPriceChangeMessage = mapper.writeValueAsString(productPriceChange);
		ProducerRecord<String, String> record = new ProducerRecord<>(KafkaTopicNames.PRODUCT_UPDATE_TOPIC, "1", productPriceChangeMessage);
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
