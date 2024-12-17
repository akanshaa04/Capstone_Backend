package com.ecommerce.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.ecommerce.domain.UserRole;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class DataInitializationComponent implements CommandLineRunner{
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final PasswordEncoder passwordEncoder;
	
	@Override
	public void run(String... args) {
		initializeAdminUser();
	}

	private void initializeAdminUser() {
		String adminUsername = "aman2002kharbanda@gmail.com" ;
	
	if(userRepository.findByEmail(adminUsername)==null) {
		User adminUser = new User();
		adminUser.setPassword(passwordEncoder.encode("123456789"));
		adminUser.setFullName("Aman kharbanda");
		adminUser.setEmail(adminUsername);
		adminUser.setRole(UserRole.Role_Admin);
		
		userRepository.save(adminUser);
	}
	
	}
}