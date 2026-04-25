package com.softwarelabs.product;

import com.softwarelabs.kafka.BaseIntegrationTest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

public class CrudProductServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private CrudProductService crudProductService;

    @Test
    public void returnProductName_ifProductSavedBefore() {
        String productName = "product001";
        BigDecimal price = BigDecimal.TEN;
        long productId = new Random().nextLong();
        Product product = new PersistantProduct(productName, productId, price);

        crudProductService.saveProduct(product);
        Product actualProduct = crudProductService.getProductByName(product.name()).get();
        assertNotNull(actualProduct);
        assertEquals(productName, actualProduct.name());
    }

    @Test
    public void returnEmptyProduct_ifProductNotExist() {
        String productName = "product002";
        BigDecimal price = BigDecimal.TEN;
        Product product = new ProductPort.ProductRequest(productName, price);

        assertFalse(crudProductService.getProductByName(product.name()).isPresent());
    }
}
