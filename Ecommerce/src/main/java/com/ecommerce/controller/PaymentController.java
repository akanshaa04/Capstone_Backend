package com.ecommerce.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.model.Order;
import com.ecommerce.model.PaymentOrder;
import com.ecommerce.model.Seller;
import com.ecommerce.model.SellerReport;
import com.ecommerce.model.User;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.response.PaymentLinkResponse;
import com.ecommerce.service.PaymentService;
import com.ecommerce.service.SellerReportService;
import com.ecommerce.service.SellerService;
import com.ecommerce.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {
	@Autowired
	private final PaymentService paymentService;
	@Autowired
	private final UserService userService;
	@Autowired
	private final SellerService sellerService;
	@Autowired
	private final SellerReportService sellerReportService;
	@GetMapping("/{paymentId}")
	public ResponseEntity<ApiResponse> paymentSuccessHandler(@PathVariable String paymentId,@RequestParam String paymentLinkId, @RequestHeader("Authorization") String jwt)throws Exception{
		User user = userService.findUserByJwtToken(jwt);
		PaymentLinkResponse paymentLinkResponse;
		
		PaymentOrder paymentOrder = paymentService.getPaymentOrderByPaymentId(paymentLinkId);
		
		for(Order order : paymentOrder.getOrders()) {
			Seller seller = sellerService.getSellerById(order.getSellerId());
			SellerReport report = sellerReportService.getSellerReport(seller);
			report.setTotalOrders(report.getTotalOrders()+1);
			report.setTotalEarnings(report.getTotalEarnings()+order.getTotalSellingPrice());
			report.setTotalSales(report.getTotalSales()+order.getOrderItems().size());
			sellerReportService.updateSellerReport(report);
		}
		ApiResponse res = new ApiResponse();
		res.setMessage("Payment Successful");
		return new ResponseEntity<>(res,HttpStatus.CREATED);
	}
}