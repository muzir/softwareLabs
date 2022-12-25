package com.softwarelabs.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.softwarelabs.kafka.EventProducer;
import com.softwarelabs.kafka.KafkaProducerFactory;
import com.softwarelabs.kafka.KafkaTopicNames;
import jakarta.annotation.PreDestroy;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.producer.Callback;
import org.apache.kafka.clients.producer.Producer;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.clients.producer.RecordMetadata;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


@Slf4j
@Service
public class ProductProducer implements EventProducer<String> {

    private final Producer<String, String> kafkaProducer;

    private final ObjectMapper mapper;

    private final Callback produceCallback = new ProduceCallback();

    @Autowired
    public ProductProducer(KafkaProducerFactory<String, String> kafkaProducerFactory, ObjectMapper mapper) {
        this.kafkaProducer = kafkaProducerFactory.createProducer(this.producerClientId());
        this.mapper = mapper;
    }

    public void publishProductChange(Product product) throws JsonProcessingException {
        ProductChange productChange = new ProductChange(product.name(), product.price());
        String productChangeMessage = mapper.writeValueAsString(productChange);
        publish(productChangeMessage);
    }

    @Override
    public void publish(String message) {
        ProducerRecord<String, String> record = new ProducerRecord<>(topicName(), "1", message);
        log.info("Publish topic : {} message : {}", topicName(), message);
        kafkaProducer.send(record, produceCallback);
    }

    @Override
    public String topicName() {
        return KafkaTopicNames.PRODUCT_CHANGE_TOPIC;
    }

    @Override
    public String producerClientId() {
        return "productClientId";
    }

    @PreDestroy
    @Override
    public void close() {
        kafkaProducer.flush();
        kafkaProducer.close();
    }

    private class ProduceCallback implements Callback {

        @Override
        public void onCompletion(RecordMetadata metadata, Exception exception) {
            if (exception != null) {
                log.error("Message can't be sent", exception);
                return;
            }
            log.info("Producer topic {}, partition {}, offset {}, messageArrivedTime",
                    metadata.topic(),
                    metadata.partition(),
                    metadata.offset(),
                    metadata.timestamp());
        }
    }
}
