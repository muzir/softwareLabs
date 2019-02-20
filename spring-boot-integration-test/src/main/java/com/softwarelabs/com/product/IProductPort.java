package com.softwarelabs.com.product;

import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

public interface IProductPort {

  @RequestMapping(
      value = "/v1/product",
      method = RequestMethod.POST,
      produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
  @ResponseBody
  ProductResponse createProduct(@RequestBody ProductRequest request);

  @RequestMapping(
      value = "/v1/product",
      method = RequestMethod.GET,
      produces = {MediaType.APPLICATION_JSON_UTF8_VALUE})
  @ResponseBody
  ProductResponse getProductById(@RequestBody ProductRequest request);

  @Data
  @Accessors(chain = true)
  class ProductResponse {
    ProductDto product;
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
    private Long id;
    private String name;
  }
}
