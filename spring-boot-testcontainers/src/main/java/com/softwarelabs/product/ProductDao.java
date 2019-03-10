package com.softwarelabs.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductDao extends JpaRepository<ProductInDb, Long> {
	Optional<ProductInDb> findByName(String productName);
}
