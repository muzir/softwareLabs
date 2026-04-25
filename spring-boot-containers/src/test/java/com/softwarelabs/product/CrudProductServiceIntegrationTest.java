package com.softwarelabs.product;

import com.softwarelabs.config.BaseIntegrationTest;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.Optional;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CrudProductServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private ProductRepository productRepository;

    @Test
    public void returnProductName_ifProductSavedBefore() {
        String productName = "product001";
        PersistantProduct product = new PersistantProduct();
        product.setId(new Random().nextLong());
        product.setName(productName);
        productRepository.save(product);
        Optional<Product> actualProduct = productRepository.findByName(productName);
        assertTrue(actualProduct.isPresent());
        assertEquals(productName, actualProduct.get().name());
    }
}
