package com.softwarelabs.config;

import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.OffsetAndMetadata;
import org.apache.kafka.clients.consumer.OffsetCommitCallback;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.DefaultKafkaConsumerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

@Configuration
@Profile("!integration")
@Slf4j
public class KafkaConfiguration {

	@Bean
	public ConsumerFactory<?, ?> kafkaConsumerFactory(
			@Value("${kafka.bootstrap.servers}") String bootstrapServers) {

		final Map props = new HashMap();
		props.put("bootstrap.servers", bootstrapServers);
		props.put("key.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("value.deserializer", "org.apache.kafka.common.serialization.StringDeserializer");
		props.put("enable.auto.commit", "false");
		props.put("auto.offset.reset", "earliest");
		props.put("max.poll.records", 50);
		return new DefaultKafkaConsumerFactory<>(props);
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
