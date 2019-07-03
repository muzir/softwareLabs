package com.softwarelabs.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<PersistantProduct, Long> {
	Optional<Product> findByName(String productName);
}
