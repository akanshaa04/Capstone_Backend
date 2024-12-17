package com.ecommerce.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.ecommerce.model.Product;
import com.ecommerce.model.Review;
import com.ecommerce.model.User;
import com.ecommerce.request.CreateReviewRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.ReviewService;
import com.ecommerce.service.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReviewController {
	@Autowired
	private final ReviewService reviewService;
	@Autowired
	private final UserService userService;
	@Autowired
	private final ProductService productService;

	@GetMapping("/products/{productId}/reviews")
	public ResponseEntity<List<Review>> getReviewsByProductId(@PathVariable Long productId) {
		List<Review> reviews = reviewService.getReviewByProductId(productId);
		return ResponseEntity.ok(reviews);
	}

	@PostMapping("/products/{productId}/reviews")
	public ResponseEntity<Review> writeReview(@RequestBody CreateReviewRequest req, @PathVariable Long productId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		System.out.println(req.getReviewText() + " " + req.getReviewRating());
		System.out.println(jwt);
		System.out.println(productId);
		User user = userService.findUserByJwtToken(jwt);
		Product product = productService.findProductById(productId);
		Review review = reviewService.createReview(req, user, product);
		return ResponseEntity.ok(review);
	}

	@PatchMapping("/reviews/{reviewId}")
	public ResponseEntity<Review> updateReview(@RequestBody CreateReviewRequest req, @PathVariable Long reviewId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		Review review = reviewService.updateReview(reviewId, req.getReviewText(), req.getReviewRating(), user.getId());
		return ResponseEntity.ok(review);
	}

	@DeleteMapping("/reviews/{reviewId}")
	public ResponseEntity<ApiResponse> deleteReview(@PathVariable Long reviewId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		reviewService.deleteReview(reviewId, user.getId());
		ApiResponse res = new ApiResponse();
		res.setMessage("Review deleted successfully");
		return ResponseEntity.ok(res);
	}
}