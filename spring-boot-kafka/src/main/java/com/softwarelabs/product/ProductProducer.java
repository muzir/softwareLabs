package com.softwarelabs.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.kafka.KafkaTopicNames;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductProducer {

	private final Producer<String, String> kafkaProducer;

	private final ObjectMapper mapper;

	@Autowired
	public ProductProducer(Producer<String, String> kafkaProducer, ObjectMapper mapper) {
		this.kafkaProducer = kafkaProducer;
		this.mapper = mapper;
	}

	public void publishProductChange(Product product) throws JsonProcessingException {
		ProductChange productChange = new ProductChange(product.name(), product.price());
		String productChangeMessage = mapper.writeValueAsString(productChange);
		ProducerRecord<String, String> record = new ProducerRecord<>(KafkaTopicNames.PRODUCT_CHANGE_TOPIC, "1", productChangeMessage);
		kafkaProducer.send(record, new ProduceCallback());
	}

	private class ProduceCallback implements Callback {

		@Override public void onCompletion(RecordMetadata metadata, Exception exception) {
			if (exception != null) {
				log.error("Message can't be sent", exception);
				return;
			}
			log.info("Producer topic {}, partition {}, offset {}, messageArrivedTime",
					metadata.topic(),
					metadata.partition(),
					metadata.offset(),
					metadata.timestamp());
		}
	}
}
