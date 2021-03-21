package com.softwarelabs.product;

import com.softwarelabs.kafka.BaseIntegrationTest;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import org.junit.Assert;
import org.junit.Test;
import org.junit.jupiter.api.Assertions;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.TransactionSystemException;
import org.testcontainers.containers.ToxiproxyContainer;

import java.io.IOException;
import java.math.BigDecimal;

@RunWith(SpringRunner.class)
public class CrudProductServiceIntegrationTest extends BaseIntegrationTest {

	@Autowired
	private CrudProductService crudProductService;

	@Autowired
	private ToxiproxyContainer.ContainerProxy proxy;

	@Test
	public void returnProductName_ifProductSavedBefore() {
		String productName = "product001";
		BigDecimal price = BigDecimal.TEN;
		Product product = new ProductPort.ProductRequest(productName, price);

		crudProductService.saveProduct(product);
		Product actualProduct = crudProductService.getProduct(product).get();
		Assert.assertNotNull(actualProduct);
		Assert.assertEquals(productName, actualProduct.name());
	}

	@Test
	public void returnEmptyProduct_ifProductNotExist() {
		String productName = "product002";
		BigDecimal price = BigDecimal.TEN;
		Product product = new ProductPort.ProductRequest(productName, price);

		Assert.assertFalse(crudProductService.getProduct(product).isPresent());
	}

	@Test
	public void throwTransactionSystemException_whenProxySetTimeout() throws IOException {
		proxy.toxics().timeout("bla", ToxicDirection.DOWNSTREAM, 1000);
		String productName = "product003";
		BigDecimal price = BigDecimal.TEN;
		Product product = new ProductPort.ProductRequest(productName, price);
		Assertions.assertThrows(TransactionSystemException.class, () -> {
			crudProductService.saveProduct(product);
		});
		proxy.toxics().get("bla").remove();
		crudProductService.saveProduct(product);
		Assert.assertTrue(crudProductService.getProduct(product).isPresent());
	}
}
