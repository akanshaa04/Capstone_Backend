package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Product;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem,Long>{
	CartItem findItemByCartAndProductAndSize(Cart cart,Product product,String size);
}
