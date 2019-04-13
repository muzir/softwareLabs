package com.softwarelabs.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.config.KafkaTopicNames;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductProducer {

	@Autowired
	private Producer<String, String> kafkaProducer;

	@Autowired
	private ObjectMapper mapper;

	public void publishProductChange(Product product) throws JsonProcessingException {
		ProductChange productChange = new ProductChange(product.name(), product.price());
		log.info("ProductChange {}", productChange.toString());
		String productChangeMessage = mapper.writeValueAsString(productChange);
		ProducerRecord<String, String> record = new ProducerRecord<>(KafkaTopicNames.PRODUCT_UPDATE_TOPIC, "1", productChangeMessage);
		kafkaProducer.send(record, new ProduceCallback());
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
