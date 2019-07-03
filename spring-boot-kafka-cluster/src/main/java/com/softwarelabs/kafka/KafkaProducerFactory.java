package com.softwarelabs.kafka;

import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerConfig;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KafkaProducerFactory<K, V> {

	private final Map<String, Object> producerProps;

	public KafkaProducerFactory(Map<String, Object> producerProps) {
		this.producerProps = new ConcurrentHashMap<>(producerProps);
	}

	public Producer<K, V> createProducer(String producerClientId) {
		producerProps.put(ProducerConfig.CLIENT_ID_CONFIG, producerClientId);
		return new KafkaProducer<K, V>(producerProps);
	}
}
