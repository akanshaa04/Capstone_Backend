package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.model.Wishlist;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.UserService;
import com.ecommerce.service.WishListService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/wishlist")
public class WishlistController {
	@Autowired
	private final WishListService wishlistService;
	@Autowired
	private final UserService userService;
	@Autowired
	private final ProductService productService;

	@PostMapping("/create")
	public ResponseEntity<Wishlist> createWishlist(@RequestHeader("Authorization") String jwt) throws Exception {
		User user1 = userService.findUserByJwtToken(jwt);
		Wishlist wishlist = wishlistService.createWishlist(user1);
		return ResponseEntity.ok(wishlist);
	}

	@GetMapping()
	public ResponseEntity<Wishlist> getwishlistByUserId(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		Wishlist wishlist = wishlistService.getWishlistByUserId(user);
		return ResponseEntity.ok(wishlist);
	}

	@PostMapping("/add-product/{productId}")
	public ResponseEntity<Wishlist> addProductToWishlist(@PathVariable Long productId,
			@RequestHeader("Authorization") String jwt) throws Exception {
		Product product = productService.findProductById(productId);
		User user = userService.findUserByJwtToken(jwt);
		Wishlist updateWishlist = wishlistService.addProductToWishlist(user, product);

		return ResponseEntity.ok(updateWishlist);
	}
}