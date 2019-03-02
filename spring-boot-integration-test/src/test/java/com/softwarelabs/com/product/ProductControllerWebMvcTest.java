package com.softwarelabs.com.product;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class ProductControllerWebMvcTest {

	private static final Long productId = 1L;

	@Autowired private MockMvc mockMvc;
	@Autowired private ObjectMapper objectMapper;
	@MockBean private ProductService productService;
	@MockBean private ProductMapper productMapper;

	@Test
	public void createProductReturnHttpStatusCode200_ifProductIsValid() throws Exception {
		String productName = "Product-1";
		IProductPort.ProductRequest productRequest =
				new IProductPort.ProductRequest().setId(productId).setName(productName);
		String json = objectMapper.writeValueAsString(productRequest);

		Product product = new Product(productId, productName);
		ProductDto productDto = new ProductDto(productName);

		when(productService.createProduct(any(), any())).thenReturn(product);
		when(productMapper.mapToProductDto(product)).thenReturn(productDto);

		this.mockMvc
				.perform(
						post("/v1/product")
								.contentType(MediaType.APPLICATION_JSON)
								.content(json)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().string(containsString("Success")))
				.andExpect(content().string(containsString(productId.toString())))
				.andExpect(content().string(containsString(productName)));
	}

	@Test
	public void returnProductWithHttpStatusCode200_ifProductIsExist() throws Exception {
		String productName = "Product-" + productId;
		Product product = new Product(productId, productName);
		ProductDto productDto = new ProductDto(productName);

		when(productService.getProduct(any())).thenReturn(product);
		when(productMapper.mapToProductDto(product)).thenReturn(productDto);

		this.mockMvc
				.perform(
						get("/v1/product" + productId)
								.contentType(MediaType.APPLICATION_JSON)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(content().string(containsString("Success")))
				.andExpect(content().string(containsString(productId.toString())))
				.andExpect(content().string(containsString(productName)));
	}

}
