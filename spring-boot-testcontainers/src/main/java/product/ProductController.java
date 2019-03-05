package product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;

@Controller
@Slf4j
public class ProductController implements IProductPort {

	private final ProductService productService;

	public ProductController(ProductService productService) {
		this.productService = productService;
	}

	@Override
	public ProductResponse createProduct(ProductRequest request) {
		Product product = productService.createProduct(request.getName(), request.getId());
		ProductResponse response = new ProductResponse();
		response.setProduct(product);
		response.setResult(new IProductPort.Result().setMessage("Success").setSuccess(true));
		return response;
	}

	@Override
	public ProductResponse getProductById(Long productId) {
		log.info(productId.toString());
		Product product = productService.getProduct(productId);
		IProductPort.ProductResponse response = new ProductResponse();
		response.setProduct(product);
		response.setResult(new IProductPort.Result().setMessage("Success").setSuccess(true));
		return response;
	}
}
