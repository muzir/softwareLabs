package com.softwarelabs.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.KafkaConsumer;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Slf4j
public class KafkaConsumerFactory<K, V> {

	private final Map<String, Object> consumerProps;

	public KafkaConsumerFactory(Map<String, Object> consumerProps) {
		this.consumerProps = new ConcurrentHashMap<>(consumerProps);
	}

	public Consumer<K, V> createConsumer(String consumerGroupId) {
		log.info("Create new KafkaConsumer");
		consumerProps.put(ConsumerConfig.GROUP_ID_CONFIG, consumerGroupId);
		return new KafkaConsumer<K, V>(consumerProps);
	}
}
