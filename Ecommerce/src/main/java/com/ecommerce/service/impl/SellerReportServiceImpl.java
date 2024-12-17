package com.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.model.Seller;
import com.ecommerce.model.SellerReport;
import com.ecommerce.repository.SellerReportRepository;
import com.ecommerce.service.SellerReportService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class SellerReportServiceImpl implements SellerReportService {
	@Autowired
	private final SellerReportRepository sellerReportRepository;

	@Override
	public SellerReport getSellerReport(Seller seller) {
		SellerReport sr = sellerReportRepository.findBySellerId(seller.getId());
		if (sr == null) {
			SellerReport newReport = new SellerReport();
			newReport.setSeller(seller);
			return sellerReportRepository.save(newReport);
		}
		return sr;
	}

	@Override
	public SellerReport updateSellerReport(SellerReport sellerReport) {
		return sellerReportRepository.save(sellerReport);
	}

}