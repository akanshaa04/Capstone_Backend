package com.ecommerce.service.impl;

import java.time.LocalDate;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Coupon;
import com.ecommerce.model.User;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.CouponRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.CouponService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CouponServiceImpl implements CouponService{
	@Autowired
	private final CouponRepository couponRepository;
	@Autowired
	private final CartRepository cartRepository;
	@Autowired
	private final UserRepository userRepository;
	@Override
	public Cart applyCoupon(String code, double orderValue, User user) throws Exception {
		Coupon coupon = couponRepository.findByCode(code);
		
		Cart cart = cartRepository.findByUserId(user.getId());
		
		if(coupon==null) {
			throw new Exception("coupon not valid");
		}
		if(user.getUsedCoupons().contains(coupon)) {
			throw new Exception("Coupon already use");
		}
		
		if(orderValue<coupon.getMinimumOrderValue()) {
			throw new Exception("valid for minimum order value of "+coupon.getMinimumOrderValue());
		}
		
		if(coupon.isActive() && LocalDate.now().isAfter(coupon.getValidityStartDate()) && LocalDate.now().isBefore(coupon.getValidityEndDate())) {
			user.getUsedCoupons().add(coupon);
			userRepository.save(user);
			
			double discountPrice = (cart.getTotalSellingPrice()*coupon.getDiscountPercentage())/100;
			cart.setTotalSellingPrice(cart.getTotalSellingPrice()-discountPrice);
			cart.setCouponCode(code);
			cart.setDiscount(coupon.getDiscountPercentage());
			cartRepository.save(cart);
			return cart;
		}
		throw new Exception("Coupon not valid");
	}

	@Override
	public Cart removeCoupon(String code, User user) throws Exception {
		Coupon coupon = couponRepository.findByCode(code);
		if(!user.getUsedCoupons().contains(coupon)) {
			return cartRepository.findByUserId(user.getId());
		}
		user.getUsedCoupons().remove(coupon);
		userRepository.save(user);
		
		if(coupon==null) {
			throw new Exception("coupon not found...");
		}
		Cart cart = cartRepository.findByUserId(user.getId());
		double a = cart.getTotalSellingPrice();
		double b = coupon.getDiscountPercentage();
		double c = (a * 100 )/(100-b);
//		double discountPrice = (cart.getTotalSellingPrice()*coupon.getDiscountPercentage())/100;
		
//		cart.setTotalSellingPrice(cart.getTotalSellingPrice()+discountPrice);
		cart.setTotalSellingPrice(c);
		cart.setCouponCode(null);
		return cartRepository.save(cart);
	}

	@Override
	public Coupon findCouponById(Long id) throws Exception {
		
		return couponRepository.findById(id).orElseThrow(()-> new Exception("coupon not found"));
	}

	@Override
	@PreAuthorize("hasRole ('ADMIN')")
	public Coupon createCoupon(Coupon coupon) {
		
		return couponRepository.save(coupon);
	}

	@Override
	public List<Coupon> findAllCoupons() {
		return couponRepository.findAll();
	}

	@Override
	@PreAuthorize("hasRole ('ADMIN')")
	public void deleteCoupon(Long id) throws Exception {
		findCouponById(id);
		couponRepository.deleteById(id);
	}
	
	
	 public List<Coupon> getCouponsUsedByUser(User user) {
	        return couponRepository.findByUsedByUsers(user); // Fetch coupons where the user is in 'usedByUsers' list
	   }

	@Override
	public Boolean changeStatus(Long id, String s) throws Exception {
		Coupon coupon = couponRepository.findById(id).get();
		if(coupon==null) {
			throw new Exception("Coupon not found with id "+ id);
		}
		
		if(s.compareTo("false")==0)
		coupon.setActive(false);
		else
			coupon.setActive(true);
		
		couponRepository.save(coupon);
		return true;
	}
	 
}