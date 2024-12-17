package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Coupon;
import com.ecommerce.model.User;
import com.ecommerce.service.CartService;
import com.ecommerce.service.CouponService;
import com.ecommerce.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/coupons")
public class AdminCouponController {
	@Autowired
	private final CouponService couponService;
	@Autowired
	private final UserService userService;
	@Autowired
	private final CartService cartService;
	
	@PostMapping("/api")
	public ResponseEntity<Cart> applyCoupon(@RequestParam String apply, @RequestParam String code, @RequestParam double orderValue, @RequestHeader("Authorization")String jwt) throws Exception{
		User user = userService.findUserByJwtToken(jwt);
		Cart cart;
		if(apply.equals("true")) {
			cart = couponService.applyCoupon(code, orderValue, user);
		}
		else {
			cart = couponService.removeCoupon(code, user);
		}
		
		return ResponseEntity.ok(cart);
	}
	
	@PostMapping("/admin/create")
	public ResponseEntity<Coupon> createCoupon(@RequestBody Coupon coupon){
		Coupon createdCoupon = couponService.createCoupon(coupon);
		return ResponseEntity.ok(createdCoupon);
	}
	
	@DeleteMapping("/admin/delete/{id}")
	public ResponseEntity<?> deleteCoupon(@PathVariable Long id) throws Exception{
		
		System.out.println("ID->"+id);
		couponService.deleteCoupon(id);
		return ResponseEntity.ok("Coupon deleted successfully");
	}
	
	@GetMapping("/admin/all")
	public ResponseEntity<List<Coupon>> getAllCoupons(){
		List<Coupon> coupon = couponService.findAllCoupons();
		return ResponseEntity.ok(coupon);
	}
	
	 @GetMapping("/used")
	    public ResponseEntity<List<Coupon>> getUsedCoupons(@RequestHeader("Authorization") String jwt) {
	        try {
	            // Fetch the user based on the provided JWT token
	            User user = userService.findUserByJwtToken(jwt);
	            
	            // Fetch the list of coupons used by the user
	            List<Coupon> usedCoupons = couponService.getCouponsUsedByUser(user);
	            
	            // Return the list of used coupons
	            return ResponseEntity.ok(usedCoupons);
	        } catch (Exception e) {
	            return ResponseEntity.status(500).body(null); // Handle error appropriately
	        }
	    }
	 @GetMapping("/update/{id}/{s}")
	 public ResponseEntity<String> updateStatus(@PathVariable Long id , @PathVariable String s) throws Exception{
		 couponService.changeStatus(id, s);
		 return ResponseEntity.ok("Updated Successfully");
	 }
}
