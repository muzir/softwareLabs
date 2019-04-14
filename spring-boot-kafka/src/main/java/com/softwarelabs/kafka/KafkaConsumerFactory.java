package com.softwarelabs.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Map;

@Slf4j
public class KafkaConsumerFactory<K, V> {

	private final Map<String, Object> consumerProps;

	public KafkaConsumerFactory(Map<String, Object> consumerProps) {
		this.consumerProps = consumerProps;
	}

	public Consumer<K, V> createConsumer() {
		log.info("Create new KafkaConsumer");
		return new KafkaConsumer<K, V>(consumerProps);
	}
}
