package com.ecommerce.controller;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.domain.PaymentMethod;
import com.ecommerce.model.Address;
import com.ecommerce.model.Cart;
import com.ecommerce.model.CartItem;
import com.ecommerce.model.Order;
import com.ecommerce.model.OrderItem;
import com.ecommerce.model.PaymentOrder;
import com.ecommerce.model.Product;
import com.ecommerce.model.Seller;
import com.ecommerce.model.SellerReport;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.response.PaymentLinkResponse;
import com.ecommerce.service.CartService;
import com.ecommerce.service.OrderService;
import com.ecommerce.service.PaymentService;
import com.ecommerce.service.ProductService;
import com.ecommerce.service.SellerReportService;
import com.ecommerce.service.SellerService;
import com.ecommerce.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/orders")
public class OrderController {
	@Autowired
	private final OrderService orderService;
	@Autowired
	private final UserService userService;
	@Autowired
	private final CartService cartService;
	@Autowired
	private final SellerService sellerService;
	@Autowired
	private final SellerReportService sellerReportService;
	@Autowired
	private final PaymentService paymentService;
	@Autowired
	private final ProductService productService;
	
	@Autowired
	private final CartRepository cartRepository;
	

	@PostMapping()
	public ResponseEntity<PaymentLinkResponse> createOrderHandler(
	        @RequestBody Address shippingAddress,
	        @RequestHeader("Authorization") String jwt) throws Exception {

		System.out.println("in controller"+ shippingAddress);
	    // Set payment method manually to 'MANUAL'
	    PaymentMethod paymentMethod = PaymentMethod.RAZORPAY;

	    // Find the user from the JWT token
	    User user = userService.findUserByJwtToken(jwt);
	    
	    // Get the user's cart
	    Cart cart = cartService.findUserCart(user);
	    
	    // Create the order
	    Set<Order> orders = orderService.createOrder(user, shippingAddress, cart);
	    
	    // Create a payment order with a fixed payment method
	    PaymentOrder paymentOrder = paymentService.createOrder(user, orders, paymentMethod);
	    
	    cart.setCouponCode(null);
	    cartRepository.save(cart);
	    
	    // Respond with the payment link response
	    PaymentLinkResponse res = new PaymentLinkResponse();
	    return new ResponseEntity<>(res, HttpStatus.OK);
	}
	
	@GetMapping("/user")
	public ResponseEntity<List<Order>> usersOrderHistoryHandler(@RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserByJwtToken(jwt);
		List<Order> orders = orderService.usersOrderHistory(user.getId());
		return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/{orderId}")
	public ResponseEntity<Order> getOrderById(@PathVariable Long orderId, @RequestHeader("Authorization") String jwt) throws Exception{
		User user = userService.findUserByJwtToken(jwt);
		Order orders = orderService.findOrderById(orderId);
		return new ResponseEntity<>(orders,HttpStatus.ACCEPTED);
	}
	
	@GetMapping("/item/{orderItemId}")
	public ResponseEntity<OrderItem> getOrderItemById(@PathVariable Long orderItemId, @RequestHeader("Authorization")String jwt) throws Exception{
		User user = userService.findUserByJwtToken(jwt);
		OrderItem orderItem = orderService.getOrderItemById(orderItemId);
		return new ResponseEntity<>(orderItem,HttpStatus.ACCEPTED);
	}
	
	@PutMapping("/{orderId}/cancel")
	public ResponseEntity<Order> cancelOrder(@PathVariable Long orderId,@RequestHeader("Authorization")String jwt) throws Exception{
		User user = userService.findUserByJwtToken(jwt);
		Order order = orderService.cancelOrder(orderId, user);
		
		Seller seller = sellerService.getSellerById(order.getSellerId());
		SellerReport report = sellerReportService.getSellerReport(seller);
		
		report.setCanceledOrders(report.getCanceledOrders()+1);
		report.setTotalRefunds(report.getTotalRefunds()+order.getTotalSellingPrice());
		sellerReportService.updateSellerReport(report);
		
		return ResponseEntity.ok(order);
	}
	
	@GetMapping("/verify")
	public synchronized ResponseEntity<Boolean> verifyCart(@RequestHeader("Authorization")String jwt,@RequestParam Long usedWalletBalance) throws Exception{
			User user = userService.findUserByJwtToken(jwt);
			Cart cart = cartService.findUserCart(user);
			System.out.println("Wallet balance--->"+usedWalletBalance);
			Set<CartItem> li = (Set<CartItem>) cart.getCartItems();
			
			Boolean flag= true;
			
			for (CartItem cartItem : li) {
				Product product = cartItem.getProduct();
				Long id = product.getId();
				Integer quantity = productService.findProductById(id).getQuantity();
				System.out.println("cart quantity : " + cartItem.getQuantity());
				System.out.println("product quantity : " + quantity);
				if(quantity<cartItem.getQuantity()) {
					return ResponseEntity.ok(false);
				}
				System.out.println("aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa");
			}
			
			if(usedWalletBalance>user.getWallet())return ResponseEntity.ok(false);
			else {
				user.setWallet(user.getWallet()-usedWalletBalance);
				userService.updateUser(user.getId(), user);
			}
			for(CartItem cartItem: li) {
				Product product = cartItem.getProduct();
				Long id = product.getId();
				Integer quantity = productService.findProductById(id).getQuantity();
				
				product.setQuantity(quantity-cartItem.getQuantity());
				productService.updateProduct(id, product);
			}
			return ResponseEntity.ok(true);
	}
}