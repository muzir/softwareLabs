package com.softwarelabs.product;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

public interface ProductPort {

    @PostMapping(
            value = "/v1/product",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    ProductResponse createProduct(@RequestBody @Valid ProductRequest request);

    @GetMapping(
            value = "/v1/product/{productName}",
            produces = {MediaType.APPLICATION_JSON_VALUE})
    @ResponseBody
    ProductResponse getProductByName(@PathVariable("productName") String productName);

    @Data
    @Accessors(chain = true)
    class ProductResponse {
        Product product;
        Result result;
    }

    @Data
    @Accessors(chain = true)
    class Result {
        private boolean success;
        private String message;
    }

    @Data
    @Accessors(chain = true)
    class ProductRequest {
        @NotNull
        private String name;
    }
}
