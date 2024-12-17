package com.ecommerce.service;
import java.util.List;
import com.ecommerce.model.Product;
import com.ecommerce.model.Review;
import com.ecommerce.model.User;
import com.ecommerce.request.CreateReviewRequest;
public interface ReviewService {
	Review createReview(CreateReviewRequest req,User user,Product product); 
	List<Review> getReviewByProductId(Long productId);
	Review updateReview(Long reviewId,String reviewText,double rating,Long userId) throws Exception;
	void deleteReview(Long reviewId,Long userId) throws Exception;
	Review getReviewById(Long reviewId,Long userId) throws Exception;
}