package com.softwarelabs.springbootresilience4j.payment;

import lombok.Getter;

@Getter
public class PaymentDto {
    private final String amount;
    private final String id;

    public PaymentDto(Payment payment) {
        this.amount = payment.getAmount();
        this.id = payment.getId();
    }
}
