package com.ecommerce.response;

import com.ecommerce.domain.UserRole;

import lombok.Data;

@Data
public class AuthResponse {
	private String jwt;
	private String message;
	private UserRole role;
}
