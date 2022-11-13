package com.softwarelabs.config;

import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

import java.util.concurrent.Callable;

@Service
public class TransactionHelperImpl implements TransactionHelper {

	private final TransactionTemplate template;

	public TransactionHelperImpl(TransactionTemplate template) {
		this.template = template;
	}

	@Override
	public void withTransaction(Runnable r) {
		template.execute(status -> {
			r.run();
			return null;
		});
	}

	@Override
	public <T> T withTransaction(Callable<T> c) {
		return template.execute(status -> {
			try {
				return c.call();
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
		});
	}
}
