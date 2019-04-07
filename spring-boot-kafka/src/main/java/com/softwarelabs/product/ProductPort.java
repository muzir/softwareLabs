package com.softwarelabs.product;

import lombok.Getter;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;

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

	@Getter
	class ProductResponse {
		private Product product;
		private Result result;

		public ProductResponse(Product product, Result result) {
			this.product = product;
			this.result = result;
		}

	}

	@Getter
	class Result {
		private boolean success;
		private String message;

		public Result(boolean success, String message) {
			this.success = success;
			this.message = message;
		}

	}

	class ProductRequest implements Product {
		@NotNull private String name;
		@NotNull private BigDecimal price;

		public ProductRequest(String name) {
			this.name = name;
			this.price = BigDecimal.ZERO;
		}

		public ProductRequest(@NotNull String name, @NotNull BigDecimal price) {
			this.name = name;
			this.price = price;
		}

		@Override public Long id() {
			return null;
		}

		@Override public String name() {
			return name;
		}

		@Override public BigDecimal price() {
			return price;
		}
	}
}
