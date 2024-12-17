package com.ecommerce.request;

import com.ecommerce.domain.UserRole;

import lombok.Data;

@Data
public class LoginOtpRequest {
	private String email;
	private String otp;
	private UserRole role;
}
