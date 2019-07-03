package com.softwarelabs.util;

@FunctionalInterface
public interface RunnableWithException {
	void run() throws Exception;
}

