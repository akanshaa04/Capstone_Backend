package com.ecommerce.service.impl;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.domain.PaymentMethod;
import com.ecommerce.domain.PaymentOrderStatus;
import com.ecommerce.model.Order;
import com.ecommerce.model.PaymentOrder;
import com.ecommerce.model.User;
import com.ecommerce.repository.OrderRepository;
import com.ecommerce.repository.PaymentOrderRepository;
import com.ecommerce.service.PaymentService;
import com.ecommerce.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService{
	
	@Autowired
	private PaymentOrderRepository paymentOrderRepository;
	@Autowired
	private OrderRepository orderRepository;
	@Autowired
	private final UserService userService;
	@Override
	
	public PaymentOrder createOrder(User user, Set<Order> orders, PaymentMethod paymentMethod) {
		// Calculate the total amount from the orders
	    Long amount = orders.stream().mapToLong(Order::getTotalSellingPrice).sum();

	    // Create and set up the payment order
	    PaymentOrder paymentOrder = new PaymentOrder();
	    paymentOrder.setAmount(amount);
	    paymentOrder.setUser(user);
	    paymentOrder.setOrders(orders);
	    paymentOrder.setPaymentMethod(paymentMethod); // Set payment method as 'MANUAL'

	    // Save and return the payment order
	    return paymentOrderRepository.save(paymentOrder);
	}


	@Override
	public PaymentOrder getPaymentOrderById(Long orderId) throws Exception {
		return paymentOrderRepository.findById(orderId).orElseThrow(()-> new Exception("payment order not found"));
	}

	@Override
	public PaymentOrder getPaymentOrderByPaymentId(String orderId) throws Exception {
		PaymentOrder paymentOrder = paymentOrderRepository.findByPaymentLinkId(orderId);
		if(paymentOrder==null)
		{
			throw new Exception("Payment not found with provided payment link id");
		}
		return paymentOrder;
	}

}