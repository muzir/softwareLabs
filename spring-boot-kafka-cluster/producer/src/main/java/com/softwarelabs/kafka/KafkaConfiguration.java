package com.softwarelabs.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Configuration
@Profile("!integration")
@Slf4j
public class KafkaConfiguration {

	private final Map<String, Object> producerProps;

	@Autowired
	public KafkaConfiguration(@Value("${kafka.bootstrap.servers}") String bootstrapServers) {
		this.producerProps = producerProps(bootstrapServers);
	}

	private Map<String, Object> producerProps(String bootstrapServers
	) {
		final Map<String, Object> props = new ConcurrentHashMap<>();
		props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringSerializer");
		props.put(ProducerConfig.ACKS_CONFIG, "all");
		props.put(ProducerConfig.LINGER_MS_CONFIG, 1);
		props.put(ProducerConfig.BUFFER_MEMORY_CONFIG, 33554432);
		return props;
	}

	@Bean
	public KafkaProducerFactory<String, String> kafkaProducerFactory() {
		return new KafkaProducerFactory(producerProps);
	}
}
