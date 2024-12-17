package com.ecommerce.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Product;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long>,JpaSpecificationExecutor<Product>{
	Product findProductById(Long productId); 
	Page<Product> findBySellerId(Long id,Pageable pageable);
	
	@Query("SELECT p FROM Product p WHERE (:query IS NULL OR LOWER(p.title) " +
		       "LIKE LOWER(CONCAT('%', :query, '%')) ) " +
		       "OR (:query IS NULL OR LOWER(p.category.categoryId) " +
		       "LIKE LOWER(CONCAT('%', :query, '%')) )" +
		       "OR (:query IS NULL OR LOWER(p.description) " +
		       "LIKE LOWER(CONCAT('%', :query, '%')) )")
		List<Product> searchProducts(@Param("query") String query);

}
