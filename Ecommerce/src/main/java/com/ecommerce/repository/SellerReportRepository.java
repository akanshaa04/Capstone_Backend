package com.ecommerce.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.ecommerce.model.Seller;
import com.ecommerce.model.SellerReport;

public interface SellerReportRepository extends JpaRepository<SellerReport, Long>{
	SellerReport findBySellerId(Long sellerId);
}