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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

@RunWith(SpringRunner.class)
@WebMvcTest(ProductController.class)
public class WebMockTest {

  @Autowired private MockMvc mockMvc;
  @Autowired private ObjectMapper mapper;

  @MockBean private ProductService service;
  @MockBean private ProductMapper productMapper;

  @Test
  public void greetingShouldReturnMessageFromService() throws Exception {
    // given
    IProductPort.ProductRequest productRequest =
        new IProductPort.ProductRequest().setId(1L).setName("Product-1");
    String json = mapper.writeValueAsString(productRequest);
    // when
    when(service.createProduct(any(), any())).thenReturn(new Product());
    // then
    this.mockMvc
        .perform(
            post("/v1/product")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json)
                .accept(MediaType.APPLICATION_JSON))
        .andDo(print())
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(content().string(containsString("Success")));
  }
}
