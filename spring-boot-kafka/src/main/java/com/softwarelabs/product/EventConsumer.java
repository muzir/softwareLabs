package com.softwarelabs.product;

public interface EventConsumer<T> {
	void consume(T value);

	Class eventType();
}
