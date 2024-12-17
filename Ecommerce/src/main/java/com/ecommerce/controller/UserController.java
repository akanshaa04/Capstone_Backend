package com.ecommerce.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.domain.UserRole;
import com.ecommerce.model.Address;
import com.ecommerce.model.Coupon;
import com.ecommerce.model.Seller;
import com.ecommerce.model.User;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.response.SignupRequest;
import com.ecommerce.service.UserService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
public class UserController {
	@Autowired
	private final UserService userService;

	@GetMapping("users/all")
	public ResponseEntity<List<User>> getAllUsers(@RequestHeader("Authorization") String jwt) throws Exception {
		List<User> users = userService.findAllUsers();
		return ResponseEntity.ok(users);
	}

	@GetMapping("users/profile")
	public ResponseEntity<User> createUserHandler(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		return ResponseEntity.ok(user);
	}

	@GetMapping("users/getWalletBalance")
	public ResponseEntity<Long> getWalletBalance(@RequestHeader("Authorization") String jwt) throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		Long walletBalance = user.getWallet();
		return ResponseEntity.ok(walletBalance);

	}

	@PatchMapping("users/update")
	public ResponseEntity<User> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody User user)
			throws Exception {
		User profile = userService.findUserByJwtToken(jwt);
		System.out.println("ndsjafn" + profile);
		User updatedUser = userService.updateUser(profile.getId(), user);
		return ResponseEntity.ok(updatedUser);
	}

	@PostMapping("users/add-address")
	public ResponseEntity<User> addAddress(@RequestHeader("Authorization") String jwt, @RequestBody Address address)
			throws Exception {
		User user = userService.findUserByJwtToken(jwt);
		User updatedUser = userService.addAddress(user.getId(), address);
		return ResponseEntity.ok(updatedUser);
	}

	@PostMapping("users/update-wallet/{userId}/{walletAmount}")
	public ResponseEntity<Boolean> updateWallet(@PathVariable Long userId, @PathVariable Integer walletAmount)
			throws Exception {
		System.out.println(userId + " " + walletAmount);
		userService.updateUserWallet(userId, walletAmount);
		return ResponseEntity.ok(true);
	}

	@GetMapping("users/manage-wallet")
	public ResponseEntity<List<User>> getAllUsersWallet(@RequestHeader("Authorization") String jwt) throws Exception {
		List<User> users = userService.walletBalance();
		return ResponseEntity.ok(users);
	}

}
