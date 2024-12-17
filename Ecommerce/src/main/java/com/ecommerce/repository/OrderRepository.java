package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.model.Order;

public interface OrderRepository extends JpaRepository<Order,Long>{
	List<Order> findByUserId(Long userId);
	List<Order> findBySellerId(Long sellerId);
}