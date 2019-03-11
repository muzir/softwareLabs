package com.softwarelabs.product;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import static org.assertj.core.api.Assertions.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class HttpRequestTest {
	private static final Long productId = 1L;
	@LocalServerPort private int port;

	@Autowired private TestRestTemplate restTemplate;

	@Autowired private ObjectMapper objectMapper;

	@Test
	public void returnProductWithHttpStatusCode200_ifProductIsExist() throws JsonProcessingException {
		String productName = "Product-" + productId;
		IProductPort.ProductRequest productRequest =
				new IProductPort.ProductRequest().setId(productId).setName(productName);

		this.restTemplate.postForObject("http://localhost:" + port + "/v1/product", productRequest, String.class);

		assertThat(this.restTemplate.getForObject("http://localhost:" + port + "/v1/product/" + productId, String.class))
				.contains(productId.toString())
				.contains(productName);
	}
}
