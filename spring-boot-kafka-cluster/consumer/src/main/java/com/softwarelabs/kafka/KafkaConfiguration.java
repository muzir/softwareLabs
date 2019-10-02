package com.softwarelabs.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
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
	private final Map<String, Object> consumerProps;

	private final String clientId;

	@Autowired
	public KafkaConfiguration(@Value("${kafka.bootstrap.servers}") String bootstrapServers, @Value("${spring.kafka.clientId}") String clientId) {
		this.clientId = clientId;
		this.consumerProps = consumerProps(bootstrapServers);
	}

	private Map<String, Object> consumerProps(
			String bootstrapServers) {

		final Map<String, Object> props = new ConcurrentHashMap<>();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 50);
		props.put(ConsumerConfig.CLIENT_ID_CONFIG, clientId);
		return props;
	}

	@Bean
	public KafkaConsumerFactory<String, String> kafkaConsumerFactory() {
		return new KafkaConsumerFactory<>(consumerProps);
	}
}
