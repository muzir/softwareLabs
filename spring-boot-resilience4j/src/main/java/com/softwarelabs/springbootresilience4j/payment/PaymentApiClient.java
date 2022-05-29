package com.softwarelabs.springbootresilience4j.payment;

import feign.Headers;
import feign.Param;
import feign.RequestLine;

@Headers("Content-Type: application/json")
public interface PaymentApiClient {
    @RequestLine("GET /api/transactions/{transactionId}")
    Payment getTransaction(@Param("transactionId") String transactionId);

    @RequestLine("POST /api/transaction")
    Payment createTransaction();
}


