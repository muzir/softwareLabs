package com.softwarelabs.product;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

public interface IProductPort {

	@PostMapping(
			value = "/v1/product",
			produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody
	ProductResponse createProduct(@RequestBody @Valid ProductRequest request);

	@GetMapping(
			value = "/v1/product/{productId}",
			produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
	@ResponseBody
	ProductResponse getProductById(@PathVariable("productId") Long productId);

	@Data
	@Accessors(chain = true) class ProductResponse {
		ProductDto product;
		Result result;
	}

	@Data
	@Accessors(chain = true) class Result {
		private boolean success;
		private String message;
	}

	@Data
	@Accessors(chain = true) class ProductRequest {
		@NotNull private Long id;
		@NotNull private String name;
	}
}
