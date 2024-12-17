package com.ecommerce.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartItemRepository;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.service.CartService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService{
	@Autowired
	private final CartRepository cartRepository;
    @Autowired
    private final CartItemRepository cartItemRepository;
    
	public CartItem addCartItem(User user, Product product, String size, int quantity) {
		Cart cart = cartRepository.findByUserId(user.getId());
		CartItem isPresent = cartItemRepository.findItemByCartAndProductAndSize(cart, product, size);
		
		if(isPresent==null) {
			CartItem cartItem = new CartItem();
			cartItem.setProduct(product);
			cartItem.setUserId(user.getId());
			cartItem.setQuantity(quantity);
			cartItem.setSize(size);
			
			int totalPrice = quantity*product.getSellingPrice();
			cartItem.setSellingPrice(totalPrice);
			cartItem.setMrpPrice(quantity*product.getMrpPrice());
			cart.getCartItems().add(cartItem);
			cartItem.setCart(cart);
			
			return cartItemRepository.save(cartItem);
		}
		else {
			Set<CartItem>setCartItem = cart.getCartItems();
		}
		return null;
	}

	@Override
	public Cart findUserCart(User user) {
		Cart cart = cartRepository.findByUserId(user.getId());
		
		int totalPrice = 0;
		int totalSellingPrice = 0;
		int totalItem = 0;
		
		for(CartItem cartItem : cart.getCartItems()) {
			totalPrice+=cartItem.getMrpPrice();
			totalSellingPrice+=cartItem.getSellingPrice();
			totalItem+=cartItem.getQuantity();
		}
		
		cart.setTotalMrpPrice(totalPrice);
		cart.setTotalItem(totalItem);
		cart.setTotalSellingPrice(totalSellingPrice);
		cart.setDiscount(discount(totalPrice,totalSellingPrice));
		return cartRepository.save(cart);
	}

	private double discount(int totalPrice, int totalSellingPrice) 
	{
		double discountPrice = totalPrice-totalSellingPrice;
		if(totalPrice>0) {
		double discount = ((discountPrice*1.0)/totalPrice)*100;
		return discount;
		}
		return 0;
	}
	
}