package com.softwarelabs.product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Slf4j
@Controller
public class ProductController implements IProductPort {

	private final ProductService productService;

	@Autowired
	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@Override
	public ProductResponse createProduct(ProductRequest request) {
		Product product = productService.createProduct(request.getName());
		ProductResponse response = new ProductResponse();
		response.setProduct(product);
		response.setResult(new IProductPort.Result().setMessage("Success").setSuccess(true));
		return response;
	}

	@Override
	public ProductResponse getProductByName(String productName) {
		Product product = productService.getProduct(productName);
		IProductPort.ProductResponse response = new ProductResponse();
		response.setProduct(product);
		response.setResult(new IProductPort.Result().setMessage("Success").setSuccess(true));
		return response;
	}
}
