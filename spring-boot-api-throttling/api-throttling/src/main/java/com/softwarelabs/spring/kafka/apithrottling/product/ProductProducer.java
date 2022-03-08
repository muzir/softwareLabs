package com.softwarelabs.spring.kafka.apithrottling.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.spring.kafka.apithrottling.kafka.KafkaTopicNames;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.PreDestroy;

@Slf4j
@Service
public class ProductProducer {

    private final KafkaTemplate<String, String> producer;

    private final ObjectMapper mapper;

    @Autowired
    public ProductProducer(KafkaTemplate<String, String> producer,
                           ObjectMapper mapper) {
        this.producer = producer;
        this.mapper = mapper;
    }

    public void publishProductOrderRequest(ProductOrderRequest productOrderRequest) throws JsonProcessingException {
        String productOrderRequestMessage = mapper.writeValueAsString(productOrderRequest);
        publish(productOrderRequestMessage);
    }

    public void publish(String message) {
        //log.info("Publish topic : {} message : {}", topicName(), message);
        producer.send(topicName(), "1", message);
    }


    public String topicName() {
        return KafkaTopicNames.PRODUCT_ORDER_REQUEST_TOPIC;
    }

    @PreDestroy
    public void close() {
        producer.flush();
        producer.destroy();
    }
}
