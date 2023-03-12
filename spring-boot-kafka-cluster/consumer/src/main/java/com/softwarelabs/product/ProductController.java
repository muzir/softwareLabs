package com.softwarelabs.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ProductController implements ProductPort {

	private final ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@Override
	public ProductResponse createProduct(ProductRequest request) {
		Product product = productService.saveProduct(request);
		ProductResponse response = new ProductResponse(product, new ProductPort.Result(true, "Success"));
		return response;
	}

	@Override
	public ProductResponse getProductByName(String productName) {
		ProductRequest productRequest = new ProductRequest(productName);
		Product product = productService.getProductByName(productRequest.name())
				.orElse(new PersistantProduct());
		ProductPort.ProductResponse response = new ProductResponse(product, new ProductPort.Result(true, "Success"));
		return response;
	}
}
