package com.ecommerce.service;

import java.util.Set;

import com.ecommerce.domain.PaymentMethod;
import com.ecommerce.model.Order;
import com.ecommerce.model.PaymentOrder;
import com.ecommerce.model.User;

public interface PaymentService {
//	   public PaymentOrder createOrder(User user, Set<Order> orders);
	   public PaymentOrder getPaymentOrderById(Long orderId) throws Exception;
	   public PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception;
	PaymentOrder createOrder(User user, Set<Order> orders, PaymentMethod paymentMethod);
}