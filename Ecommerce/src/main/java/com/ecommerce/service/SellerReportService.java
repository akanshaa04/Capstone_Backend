package com.ecommerce.service;

import com.ecommerce.model.Seller;
import com.ecommerce.model.SellerReport;

public interface SellerReportService {
	SellerReport getSellerReport(Seller seller);
	SellerReport updateSellerReport(SellerReport sellerReport);
}