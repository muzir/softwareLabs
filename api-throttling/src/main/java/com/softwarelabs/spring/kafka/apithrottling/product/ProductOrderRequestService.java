package com.softwarelabs.spring.kafka.apithrottling.product;

import com.softwarelabs.spring.kafka.apithrottling.kafka.MessageProcessingResult;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
public class ProductOrderRequestService {

    private final ProductOrderApiClientThrottlingService productOrderApiClientThrottlingService;

    public ProductOrderRequestService(
            ProductOrderApiClientThrottlingService productOrderApiClientThrottlingService) {
        this.productOrderApiClientThrottlingService = productOrderApiClientThrottlingService;
    }

    public MessageProcessingResult handleRequest(ProductOrderRequest request) {
        UUID id = request.getId();
        try {
            productOrderApiClientThrottlingService.sendProductOrderRequest(request);
        }
        /*catch (FeignException.TooManyRequests e) {
            return MessageProcessingResult.builder()
                    .success(false)
                    .shouldRetry(true)
                    .message("Execution throttled. product order id: %s"
                            .formatted(id))
                    .exception(e)
                    .build();

        }*/ catch (Exception e) {
            return MessageProcessingResult.builder()
                    .success(false)
                    .message("Failed to submit product order id: %s".formatted(id))
                    .exception(e)
                    .build();

        } finally {
            // update repository
        }

        return MessageProcessingResult.builder()
                .message("Posted product order. Product order id: %s".formatted(id))
                .success(true)
                .build();
    }
}
