package com.ecommerce.service;

import org.springframework.stereotype.Service;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;


public interface CartService {
	public CartItem addCartItem(User user,Product product,String size,int quantity);
	public Cart findUserCart(User user);
}