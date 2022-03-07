package com.softwarelabs.spring.kafka.apithrottling.kafka;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MessageProcessingResult {

    private boolean success;

    @Builder.Default
    private boolean shouldRetry = false;

    private String message;

    private Exception exception;

    public static MessageProcessingResult processingSuccess() {
        return MessageProcessingResult.builder().success(true).build();
    }

    public static MessageProcessingResult processingFailWithRetry() {
        return MessageProcessingResult.builder().success(false).shouldRetry(true).build();
    }
}
