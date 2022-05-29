package com.softwarelabs.springbootresilience4j.payment;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/payments")
public class PaymentController {

    @Autowired
    PaymentService paymentService;

    @GetMapping("/{paymentId}")
    public PaymentDto getPayment(@PathVariable String paymentId) {
        Payment payment = paymentService.getTransaction(paymentId);
        return new PaymentDto(payment);
    }
}
