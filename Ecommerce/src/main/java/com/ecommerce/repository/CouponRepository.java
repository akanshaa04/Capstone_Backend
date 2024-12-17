package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.model.Coupon;
import com.ecommerce.model.User;

@Repository
public interface CouponRepository extends JpaRepository<Coupon,Long>{
	Coupon findByCode(String code);
    List<Coupon> findByUsedByUsers(User user);

}