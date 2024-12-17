package com.ecommerce.service;

import java.util.List;

import com.ecommerce.model.Cart;
import com.ecommerce.model.Coupon;
import com.ecommerce.model.User;

public interface CouponService {
	Cart applyCoupon(String code,double orderValue,User user) throws Exception;
	Cart removeCoupon(String code,User user) throws Exception;
	Coupon findCouponById(Long id) throws Exception;
	Coupon createCoupon(Coupon coupon);
	List<Coupon> findAllCoupons();
	void deleteCoupon(Long id) throws Exception;
	List<Coupon> getCouponsUsedByUser(User user);
	Boolean changeStatus(Long id, String s) throws Exception;  
}