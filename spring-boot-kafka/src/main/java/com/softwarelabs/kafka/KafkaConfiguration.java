package com.softwarelabs.kafka;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerConfig;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Profile("!integration")
@Slf4j
public class KafkaConfiguration {

	@Bean
	public Map<String, Object> consumerProps(
			@Value("${kafka.bootstrap.servers}") String bootstrapServers) {

		final Map<String, Object> props = new HashMap();
		props.put(ConsumerConfig.BOOTSTRAP_SERVERS_CONFIG, bootstrapServers);
		props.put(ConsumerConfig.KEY_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.VALUE_DESERIALIZER_CLASS_CONFIG, "org.apache.kafka.common.serialization.StringDeserializer");
		props.put(ConsumerConfig.ENABLE_AUTO_COMMIT_CONFIG, "false");
		props.put(ConsumerConfig.AUTO_OFFSET_RESET_CONFIG, "earliest");
		props.put(ConsumerConfig.GROUP_ID_CONFIG, "consumerGroup1");
		props.put(ConsumerConfig.MAX_POLL_RECORDS_CONFIG, 50);
		return props;
	}

	@Bean
	public Producer<String, String> kafkaProducer(
			@Value("${kafka.bootstrap.servers}") String bootstrapServers) {
		final Properties props = new Properties();
		props.put("bootstrap.servers", bootstrapServers);
		props.put("key.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("value.serializer", "org.apache.kafka.common.serialization.StringSerializer");
		props.put("acks", "all");
		props.put("linger.ms", 1);
		props.put("buffer.memory", 33554432);
		props.put("clientId", "productProducer");
		return new KafkaProducer<>(props);
	}

	@Bean
	public OffsetCommitCallback errorLoggingCommitCallback() {
		return new ErrorLoggingCommitCallback();
	}

	public class ErrorLoggingCommitCallback implements OffsetCommitCallback {

		@Override
		public void onComplete(Map<TopicPartition, OffsetAndMetadata> offsets, Exception exception) {
			if (exception != null) {
				log.warn("Exception while commiting offsets to Kafka", exception);
			}
		}
	}

}
