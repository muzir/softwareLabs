package product;

import java.util.Optional;

public interface ProductService {

	Product getProduct(Long productId);

	Product createProduct(String name, Long id);
}
