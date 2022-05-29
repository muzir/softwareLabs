package com.softwarelabs.springbootresilience4j.payment;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.Instant;

@Service
@Slf4j
public class PaymentService {

    private final PaymentApiClient paymentApiClient;

    public PaymentService(PaymentApiClient paymentApiClient) {
        this.paymentApiClient = paymentApiClient;
    }

    public Payment getTransaction(String paymentId) {
        log.info("Request started at {}", Instant.now(Clock.systemUTC()));
        Payment payment = paymentApiClient.getTransaction(paymentId);
        return payment;
    }
}
