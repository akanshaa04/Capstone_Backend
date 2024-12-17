package com.ecommerce.service;

import org.springframework.stereotype.Service;

import com.ecommerce.domain.UserRole;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.response.SignupRequest;

@Service
public interface AuthService {
	void sentLoginOtp(String email,UserRole role) throws Exception;
	String createUser(SignupRequest req) throws Exception;
	AuthResponse signing(LoginRequest req);
}
