package com.softwarelabs.product;

import com.softwarelabs.kafka.BaseIntegrationTest;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.testcontainers.containers.ToxiproxyContainer;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


public class CrudProductServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CrudProductService crudProductService;

    @Autowired
    private ToxiproxyContainer.ContainerProxy jdbcDatabaseContainerProxy;

    @Test
    public void returnProductName_ifProductSavedBefore() {
        String productName = "product001";
        BigDecimal price = BigDecimal.TEN;
        Product product = new ProductPort.ProductRequest(productName, price);

        crudProductService.saveProduct(product);
        Product actualProduct = crudProductService.getProduct(product).get();
        assertNotNull(actualProduct);
        assertEquals(productName, actualProduct.name());
    }

    @Test
    public void returnEmptyProduct_ifProductNotExist() {
        String productName = "product002";
        BigDecimal price = BigDecimal.TEN;
        Product product = new ProductPort.ProductRequest(productName, price);

        assertFalse(crudProductService.getProduct(product).isPresent());
    }

    @Test
    public void throwTransactionSystemException_whenProxySetTimeout() throws IOException {
        // Cut the connection after 1 seconds
        jdbcDatabaseContainerProxy.toxics().timeout("hard-cut", ToxicDirection.DOWNSTREAM, 1000);

        String productName = "product003";
        Product product = new ProductPort.ProductRequest(productName, BigDecimal.TEN);

        // Use Exception.class first to see exactly what is thrown
        assertThrows(Exception.class, () -> {
            crudProductService.saveProduct(product);
        });

        jdbcDatabaseContainerProxy.toxics().get("hard-cut").remove();
    }

    @Test
    public void throwTransactionSystemException_whenProxySetLatency() throws IOException, InterruptedException {
        // Add 5 seconds latency
        jdbcDatabaseContainerProxy.toxics().latency("latency", ToxicDirection.DOWNSTREAM, 5000);
        // Prevent the test from finishing before the latency is applied
        //TODO  How can I remove Thread.sleep?
        Thread.sleep(1000);
        String productName = "product003";
        Product product = new ProductPort.ProductRequest(productName, BigDecimal.TEN);

        // Use Exception.class first to see exactly what is thrown
        assertThrows(Exception.class, () -> {
            crudProductService.saveProduct(product);
        });

        jdbcDatabaseContainerProxy.toxics().get("latency").remove();
    }
}
