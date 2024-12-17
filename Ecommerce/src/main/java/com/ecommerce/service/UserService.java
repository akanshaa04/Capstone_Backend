package com.ecommerce.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.ecommerce.model.Address;
import com.ecommerce.model.User;

@Service
public interface UserService{
	User findUserByJwtToken(String jwt) throws Exception;
	User findUserByEmail(String email) throws Exception;
	User updateUser(Long id, User user) throws Exception;
    User addAddress(Long userId, Address address) throws Exception;
	List<User> findAllUsers();
	void updateUserWallet(Long userId, Integer walletAmount) throws Exception;
	List<User> walletBalance();
}
