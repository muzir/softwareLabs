package com.softwarelabs.product;

import com.softwarelabs.kafka.BaseIntegrationTest;
import eu.rekawek.toxiproxy.model.ToxicDirection;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.jpa.JpaSystemException;
import org.testcontainers.containers.ToxiproxyContainer;

import java.io.IOException;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;


public class CrudProductServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CrudProductService crudProductService;

    @Autowired
    private ToxiproxyContainer.ContainerProxy jdbcDatabaseContainerProxy;

    @Autowired
    private EntityManager entityManager; // Add this to your test class

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
        //jdbcDatabaseContainerProxy.toxics().timeout("bla", ToxicDirection.DOWNSTREAM, 0);
        jdbcDatabaseContainerProxy.toxics().latency("postgresql-latency", ToxicDirection.DOWNSTREAM, 1600).setJitter(100);
        String productName = "product003";
        BigDecimal price = BigDecimal.TEN;
        Product product = new ProductPort.ProductRequest(productName, price);
        Assertions.assertThrows(JpaSystemException.class, () -> crudProductService.saveProduct(product));
        jdbcDatabaseContainerProxy.toxics().get("postgresql-latency").remove();
        crudProductService.saveProduct(product);
        assertTrue(crudProductService.getProduct(product).isPresent());
    }
}
