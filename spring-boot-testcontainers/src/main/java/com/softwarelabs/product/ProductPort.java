package com.softwarelabs.product;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface ProductPort {

	@PostMapping(
			value = "/v1/product",
			produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody
	ProductResponse createProduct(@RequestBody @Valid ProductRequest request);

	@GetMapping(
			value = "/v1/product/{productName}",
			produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody
	ProductResponse getProductByName(@PathVariable("productName") String productName);

	@Data
	@Accessors(chain = true) class ProductResponse {
		Product product;
		Result result;
	}

	@Data
	@Accessors(chain = true) class Result {
		private boolean success;
		private String message;
	}

	@Data
	@Accessors(chain = true) class ProductRequest {
		@NotNull private String name;
	}
}
