package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Wishlist;

@Repository
public interface WishlistRepository extends JpaRepository<Wishlist, Long>{
	Wishlist findByUserId(Long userId);
}