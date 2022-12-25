package com.softwarelabs.kafka;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import java.util.Set;

@Configuration
public class ConsumerConfiguration {

    private final Set<EventConsumer> consumers;
    private final KafkaConsumerFactory<String, String> kafkaConsumerFactory;

    @Autowired
    public ConsumerConfiguration(Set<EventConsumer> consumers, KafkaConsumerFactory kafkaConsumerFactory) {
        this.consumers = consumers;
        this.kafkaConsumerFactory = kafkaConsumerFactory;
    }

    @PostConstruct
    public void start() {
        consumers.forEach(consumer -> consumer.start(kafkaConsumerFactory));
    }

    @PreDestroy
    public void stop() {
        consumers.forEach(consumer -> consumer.stop());
    }

}
