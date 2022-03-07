package com.softwarelabs.springbootresilience4j;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/product-order")
public class ProductOrderController {

    @PostMapping
    ResponseEntity createProductOrder() {
        return ResponseEntity.ok().build();
    }
}
