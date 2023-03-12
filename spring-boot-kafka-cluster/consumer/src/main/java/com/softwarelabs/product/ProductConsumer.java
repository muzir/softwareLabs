package com.softwarelabs.product;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
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
                new KafkaConsumerThread(this, kafkaConsumerFactory.createConsumer(consumerGroupId()), newMapper());
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
        productService.getProductByName(productChange.name())
                .map(p -> productService.updateProduct(
                        new PersistantProduct(productChange.name(), p.id(), productChange.price())))
                .orElseGet(() -> productService.saveProduct(productChange));
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
        return "productConsumerGroup";
    }

    private ObjectMapper newMapper() {
        final ObjectMapper mapper = new ObjectMapper();
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        mapper.registerModule(new JavaTimeModule());
        mapper.registerModule(new Jdk8Module());
        return mapper;
    }
}
