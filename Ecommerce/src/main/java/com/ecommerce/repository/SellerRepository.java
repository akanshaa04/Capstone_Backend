package com.ecommerce.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.ecommerce.domain.AccountStatus;
import com.ecommerce.model.Seller;

@Repository
public interface SellerRepository extends JpaRepository<Seller, Long>{
	Seller findByEmail(String email);
	List<Seller> findByAccountStatus(AccountStatus accountStatus);
}
