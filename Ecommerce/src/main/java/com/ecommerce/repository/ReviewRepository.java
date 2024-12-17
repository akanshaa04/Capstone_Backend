package com.ecommerce.repository;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import com.ecommerce.model.Review;
public interface ReviewRepository extends JpaRepository<Review, Long>{
	List<Review>findByProductId(Long productId);
}