package com.softwarelabs.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.softwarelabs.kafka.BaseIntegrationTest;
import com.softwarelabs.kafka.KafkaTopicNames;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.junit4.SpringRunner;
import org.testcontainers.shaded.com.fasterxml.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.util.Random;

@RunWith(SpringRunner.class)
@Slf4j
public class ProductKafkaIntegrationTest extends BaseIntegrationTest {

    public static final long WAITING_TIME = 2000l;
    @Autowired
    KafkaProducer kafkaProducer;
    @Autowired
    ProductService productService;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Test
    public void updateProduct_ifProductChangeEventSent() throws JsonProcessingException, InterruptedException,
            org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException {
		/*
		 save new product to product table which name is product1
		 */
        String productName = "product1";
        BigDecimal price = new BigDecimal("22.25");
        long id = new Random().nextLong();
        Product product = new PersistantProduct(productName, id, price);
        productService.saveProduct(product);

        //Sent price change event
        BigDecimal newPrice = new BigDecimal("20.00");
        ProductChange productChange = new ProductChange(id, productName, newPrice);
        String productChangeMessage = objectMapper.writeValueAsString(productChange);
        ProducerRecord<String, String> record =
                new ProducerRecord<>(KafkaTopicNames.PRODUCT_CHANGE_TOPIC, "1", productChangeMessage);
        kafkaProducer.send(record);

        Thread.sleep(WAITING_TIME);

        //Product should be updated with new price
        Product updatedProduct = productService.getProductByName(productName).get();
        Assert.assertEquals(productName, updatedProduct.name());
        Assert.assertEquals(newPrice, updatedProduct.price());
    }

    @Test
    public void saveProduct_ifProductChangeEventSent_andProductNotExist()
            throws InterruptedException,
            org.testcontainers.shaded.com.fasterxml.jackson.core.JsonProcessingException {
        long id = new Random().nextLong();
        String productName = "product2";
        BigDecimal price = new BigDecimal("20.00");
        //Sent price change event
        Product productChange = new ProductChange(id, productName, price);
        String productChangeMessage = objectMapper.writeValueAsString(productChange);
        ProducerRecord<String, String> record =
                new ProducerRecord<>(KafkaTopicNames.PRODUCT_CHANGE_TOPIC, "1", productChangeMessage);
        kafkaProducer.send(record);

        Thread.sleep(WAITING_TIME);

        //Check product is saved
        Product savedProduct = productService.getProductByName(productName).get();
        Assert.assertEquals(productName, savedProduct.name());
        Assert.assertEquals(price, savedProduct.price());
    }
}
