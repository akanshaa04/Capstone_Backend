package com.ecommerce.service;

import java.util.List;
import java.util.Set;

import com.ecommerce.domain.OrderStatus;
import com.ecommerce.model.Address;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.User;

public interface OrderService {
	Set<Order> createOrder(User user, Address shippingAddress, Cart cart);
	Order findOrderById(Long id) throws Exception;
	List<Order> usersOrderHistory(Long userId);
	List<Order> sellersOrder(Long sellerId);
	Order updateOrderStatus(Long OrderId,OrderStatus orderStatus) throws Exception;
	Order cancelOrder(Long orderId,User user) throws Exception;
	OrderItem getOrderItemById(Long id) throws Exception;
}