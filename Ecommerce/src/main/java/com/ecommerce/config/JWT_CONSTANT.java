package com.ecommerce.config;
 
public class JWT_CONSTANT {
	// Secret key for signing and verifying the JWT
    public static final String SECRET_KEY = "182a6965-7620-4168-96aa-ab581daa2d25"; // Replace with a secure key!
    public static final String JWT_HEADER = "Authorization";
    // JWT Token validity (e.g., 10 hours)
    public static final long TOKEN_VALIDITY = 10 * 60 * 60 * 1000; // 10 hours in milliseconds
 
    // Claims
    public static final String CLAIM_USERNAME = "username";
    public static final String CLAIM_ROLES = "roles";
 
    // HTTP Headers
    public static final String TOKEN_PREFIX = "Bearer ";
 
    // Other constants
    public static final String ISSUER = "MyEcommerceApp"; // Set your application's name or domain
 
}