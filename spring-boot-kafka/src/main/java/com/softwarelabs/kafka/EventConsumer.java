package com.softwarelabs.kafka;

public interface EventConsumer<T> {

	void start(KafkaConsumerFactory kafkaConsumerFactory);

	void stop();

	void consume(T value);

	Class eventType();

	String topicName();

	String consumerGroupId();
}
