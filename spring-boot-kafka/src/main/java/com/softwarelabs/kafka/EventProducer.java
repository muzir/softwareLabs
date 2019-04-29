package com.softwarelabs.kafka;

public interface EventProducer<T> {
	void publish(T value);

	String topicName();

	String producerClientId();
}
