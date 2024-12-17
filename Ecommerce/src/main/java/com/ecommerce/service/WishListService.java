package com.ecommerce.service;

import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.model.Wishlist;

public interface WishListService {
	Wishlist createWishlist(User user);
	Wishlist getWishlistByUserId(User user);
	Wishlist addProductToWishlist(User user, Product product);
}