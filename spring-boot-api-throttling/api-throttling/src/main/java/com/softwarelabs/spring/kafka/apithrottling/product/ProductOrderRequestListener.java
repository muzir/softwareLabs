package com.softwarelabs.spring.kafka.apithrottling.product;

import com.fasterxml.jackson.core.JsonParseException;
import com.softwarelabs.spring.kafka.apithrottling.config.json.JsonMapper;
import com.softwarelabs.spring.kafka.apithrottling.kafka.MessageProcessingResult;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.stereotype.Service;

import java.util.function.Function;

import static com.softwarelabs.spring.kafka.apithrottling.kafka.KafkaTopicNames.PRODUCT_ORDER_REQUEST_TOPIC;

@Slf4j
@Service
public class ProductOrderRequestListener {

    private final ProductOrderRequestService productOrderRequestService;

    @Autowired
    public ProductOrderRequestListener(
            ProductOrderRequestService productOrderRequestService) {
        this.productOrderRequestService = productOrderRequestService;
    }

    @KafkaListener(topics = PRODUCT_ORDER_REQUEST_TOPIC)
    public void consume(ConsumerRecord<String, String> record, Acknowledgment acknowledgment) {
        handleMessage(record, ProductOrderRequest.class, acknowledgment, productOrderRequestService::handleRequest);
    }

    private <T> void handleMessage(ConsumerRecord<String, String> record, Class<T> clazz, Acknowledgment acknowledgment,
                                   Function<T, MessageProcessingResult> handler) {
        final String payload = record.value();
        final String topic = record.topic();

        //log.info("Received message: {}", record);
        try {
            final T parsedMessage = JsonMapper.DEFAULT.read(payload, clazz);
            final MessageProcessingResult result = handler.apply(parsedMessage);
            log.info("{}", result.getMessage());
            if (result.isSuccess()) {
                //log.info("topic:{} - message processing result: {}", topic, result);
                acknowledgment.acknowledge();
                return;
            }

            if (result.isShouldRetry()) {
                //log.warn("topic:{} - message processing result, will retry: {}", topic, result);
                acknowledgment.nack(0);
                return;
            }

            log.error("topic:{} - message processing result: {}", topic, result);

        } catch (Exception e) {
            if (e.getCause() instanceof JsonParseException) {
                log.error("Failed to parse incoming message: {}", payload, e);
            } else {
                log.error("Failed to process incoming message: {}", payload, e);
                throw e;
            }
        }

        acknowledgment.acknowledge();
    }
}
