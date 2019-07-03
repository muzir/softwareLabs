package com.softwarelabs.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softwarelabs.kafka.BaseIntegrationTest;
import com.softwarelabs.util.TestUtil;
import lombok.extern.slf4j.Slf4j;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.Instant;
import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.function.Predicate;

@RunWith(SpringRunner.class)
@Slf4j
public class ProductKafkaIntegrationTest extends BaseIntegrationTest {

	@Autowired
	ProductProducer productProducer;
	@Autowired
	ProductService productService;

	@Test
	public void updateProduct_ifProductChangeEventSent() throws JsonProcessingException {
		/*
		 save new product to product table which name is product1
		 */
		String productName = "product1";
		BigDecimal price = new BigDecimal("22.25");
		Product product = new PersistantProduct(null, productName, price);
		productService.saveProduct(product);

		//Sent price change event
		BigDecimal newPrice = new BigDecimal("20.00");
		Product productChange = new ProductChange(productName, newPrice);
		productProducer.publishProductChange(productChange);

		//Product should be updated with new price
		Product updatedProductParam = new PersistantProduct(productName);
		Optional<Product> updatedProduct = retryUntil(
				() -> productService.getProduct(updatedProductParam),
				l -> l.get().price().equals(newPrice));
		Assert.assertEquals(productName, updatedProduct.get().name());
	}

	@Test
	public void saveProduct_ifProductChangeEventSent_andProductNotExist() throws JsonProcessingException {
		String productName = "product2";
		BigDecimal price = new BigDecimal("20.00");
		//Sent price change event
		Product productChange = new ProductChange(productName, price);
		productProducer.publishProductChange(productChange);

		//Check product is saved
		Product paramSavedProduct = new PersistantProduct(productName);
		Optional<Product> savedProduct = retryUntil(
				() -> productService.getProduct(paramSavedProduct),
				Optional::isPresent);
		Assert.assertEquals(productName, savedProduct.get().name());
		Assert.assertEquals(price, savedProduct.get().price());
	}

	private <T> T retryUntil(Callable<T> callable, Predicate<T> predicate) {
		return retryUntil(callable, predicate, Duration.ofSeconds(10L), Duration.ofMillis(100L));
	}

	private <T> T retryUntil(Callable<T> callable, Predicate<T> predicate, Duration maxDuration, Duration checkInterval) {
		Instant start = Instant.now();
		Instant endTime = start.plus(maxDuration);

		T result;
		do {
			result = TestUtil.callUnchecked(callable);
			if (predicate.test(result)) {
				break;
			}

			try {
				Thread.sleep(checkInterval.toMillis());
			} catch (InterruptedException e) {
			}
		} while (Instant.now().isBefore(endTime));

		return result;
	}
}
