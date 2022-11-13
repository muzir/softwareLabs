package com.softwarelabs.config;

import java.util.concurrent.Callable;

public interface TransactionHelper {

	void withTransaction(Runnable r);

	<T> T withTransaction(Callable<T> c);

}
