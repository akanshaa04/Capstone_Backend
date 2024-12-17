package com.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.Exception.SellerException;
import com.ecommerce.domain.AccountStatus;
import com.ecommerce.model.Seller;

@Service
public interface SellerService {
	Seller getSellerProfile(String jwt) throws Exception;
	Seller createSeller(Seller seller)throws Exception;
	Seller getSellerById(Long Id)throws SellerException;
	Seller getSellerByEmail(String email) throws Exception;
	List<Seller> getAllSellers(AccountStatus status) throws Exception;
	Seller updateSeller(Long id , Seller seller) throws Exception;
	void deleteSeller(Long id) throws Exception;
	Seller verifyEmail(String email,String otp) throws Exception;
	Seller updateSellerAccountStatus(Long sellerId, AccountStatus status) throws Exception;
	List<Seller> AllSellers();
}
