package com.softwarelabs.kafka;

public interface EventConsumer<T> {

	void consume(T value);

	Class eventType();

	String topicName();
}
