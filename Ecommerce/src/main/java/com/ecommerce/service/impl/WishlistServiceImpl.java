package com.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.model.Wishlist;
import com.ecommerce.repository.WishlistRepository;
import com.ecommerce.service.WishListService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class WishlistServiceImpl implements WishListService {

	@Autowired
	private WishlistRepository wishlistRepo;

	@Override
	public Wishlist createWishlist(User user) {
		Wishlist wishlist = new Wishlist();
		wishlist.setUser(user);
		return wishlistRepo.save(wishlist);
	}

	@Override
	public Wishlist getWishlistByUserId(User user) {
		Wishlist wishlist = wishlistRepo.findByUserId(user.getId());
		if (wishlist == null) {
			wishlist = createWishlist(user);
		}
		return wishlist;
	}

	@Override
	public Wishlist addProductToWishlist(User user, Product product) {
		Wishlist wishlist = getWishlistByUserId(user);

		if (wishlist.getProducts().contains(product)) {
			wishlist.getProducts().remove(product);
		} else
			wishlist.getProducts().add(product);
		return wishlistRepo.save(wishlist);
	}

}