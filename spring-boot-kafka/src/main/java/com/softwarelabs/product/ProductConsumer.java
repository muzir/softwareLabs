package com.softwarelabs.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.kafka.EventConsumer;
import com.softwarelabs.kafka.KafkaConsumerFactory;
import com.softwarelabs.kafka.KafkaConsumerThread;
import com.softwarelabs.kafka.KafkaTopicNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class ProductConsumer implements EventConsumer<ProductChange> {

    private final ProductService productService;

    private KafkaConsumerThread<ProductChange, String, String> productConsumerThread;

    @Autowired
    public ProductConsumer(ProductService productService) {
        this.productService = productService;

    }

    @Override
    public void start(KafkaConsumerFactory kafkaConsumerFactory) {
        productConsumerThread =
                new KafkaConsumerThread(this, kafkaConsumerFactory.createConsumer(consumerGroupId()),
                        new ObjectMapper());
        productConsumerThread.start();
    }

    @Override
    public void stop() {
        if (productConsumerThread != null) {
            log.info("Product consumer is stopping");
            productConsumerThread.stop();
        }
    }

    @Override
    public void consume(ProductChange productChange) {
        log.info("Consume productChange name: {}  price: {}", productChange.name(), productChange.price());
        Product product = new PersistantProduct(productChange);
        productService.getProduct(product).ifPresentOrElse((p -> {
                    log.info("Product {} is exist", product.name());
                    productService.updateProductPrice(productChange.name(), productChange.price());
                }),
                () -> {
                    log.info("Product {} is not exist", product.name());
                    productService.saveProduct(productChange);
                });
    }

    @Override
    public Class eventType() {
        return ProductChange.class;
    }

    @Override
    public String topicName() {
        return KafkaTopicNames.PRODUCT_CHANGE_TOPIC;
    }

    @Override
    public String consumerGroupId() {
        return "consumerGroup1";
    }
}
