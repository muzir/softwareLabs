package product;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {

	private final ProductDao productDao;

	public ProductServiceImpl(ProductDao productDao) {
		this.productDao = productDao;
	}

	@Override
	public Product getProduct(Long productId) {
		ProductInDb productInDb = productDao.findById(productId).orElseThrow(
				() -> new RuntimeException(
						"Product is not found by productId #:" + productId
				)
		);
		return productInDb;
	}

	@Override
	public ProductInDb createProduct(String name, Long id) {
		ProductInDb product = new ProductInDb(id, name);
		return productDao.save(product);
	}
}
