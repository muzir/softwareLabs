package com.softwarelabs.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KafkaProducerFactory<K, V> {

	private final Map<String, Object> producerProps;

	public KafkaProducerFactory(Map<String, Object> producerProps) {
		this.producerProps = new ConcurrentHashMap<>(producerProps);
	}

	public Producer<K, V> createProducer() {
		return new KafkaProducer<K, V>(producerProps);
	}
}
