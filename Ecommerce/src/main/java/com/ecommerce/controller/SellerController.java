package com.ecommerce.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.ecommerce.Exception.SellerException;
import com.ecommerce.config.JwtProvider;
import com.ecommerce.domain.AccountStatus;
import com.ecommerce.model.Seller;
import com.ecommerce.model.VerificationCode;
import com.ecommerce.repository.VerificationCodeRepository;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.response.SignupRequest;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.EmailService;
import com.ecommerce.service.SellerService;
import com.ecommerce.util.OtpUtil;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/seller")
public class SellerController {
	private final SellerService sellerService;
	private final VerificationCodeRepository verificationCodeRepository;
	private final AuthService authService;
	private final EmailService emailService;
	private final JwtProvider jwtProvider;

	@PostMapping("/login")
	public ResponseEntity<AuthResponse> loginSeller(@RequestBody LoginRequest req) throws Exception {
		String otp = req.getOtp();
		String email = req.getEmail();

		req.setEmail("seller_" + email);
		AuthResponse authResponse = authService.signing(req);

		return ResponseEntity.ok(authResponse);

	}

	@PatchMapping("/verify/{otp}")
	public ResponseEntity<String> verifySellerEmail(@PathVariable String otp) throws Exception {
		VerificationCode verificationCode = verificationCodeRepository.findByOtp(otp);

		if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
			throw new Exception("wrong otp");
		}

		Seller seller = sellerService.verifyEmail(verificationCode.getEmail(), otp);

		return new ResponseEntity<>("Your email has been verified successfully!", HttpStatus.OK);
	}

	@PostMapping
	public ResponseEntity<Seller> createSeller(@RequestBody Seller seller) throws Exception {
		Seller savedSeller = sellerService.createSeller(seller);

		String otp = OtpUtil.generateOtp();

		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setOtp(otp);
		verificationCode.setEmail(seller.getEmail());
		verificationCodeRepository.save(verificationCode);

		String subject = "Seller Verification Code";
		String text = "Welcome to Shopping app, Verify using this link ";
//		String frontend_url = "http://localhost:3000/verify-seller/";
		String frontend_url = "http://localhost:3000/verify-seller/?otp=" + otp;

		emailService.sendVerificationOtpEmail(seller.getEmail(), verificationCode.getOtp(), subject,
				text + frontend_url);
		return new ResponseEntity<>(savedSeller, HttpStatus.CREATED);

	}

	@GetMapping("/{id}")
	public ResponseEntity<Seller> getSellerById(@PathVariable Long id) throws SellerException {
		Seller seller = sellerService.getSellerById(id);
		return new ResponseEntity<>(seller, HttpStatus.OK);
	}

	@GetMapping("/profile")
	public ResponseEntity<Seller> getSellerByJwt(@RequestHeader("Authorization") String jwt) throws Exception {
		Seller seller = sellerService.getSellerProfile(jwt);
		return new ResponseEntity<>(seller, HttpStatus.OK);
	}

	@GetMapping("/all")
	public ResponseEntity<List<Seller>> getAllSellers(@RequestParam(required = false) AccountStatus status)
			throws Exception {
		System.out.println("------->" + status);
		List<Seller> sellers = sellerService.getAllSellers(status);
		return ResponseEntity.ok(sellers);
	}

	@GetMapping("/allSeller")
	public ResponseEntity<List<Seller>> getAllSellers() {
		try {
			List<Seller> sellers = sellerService.AllSellers();
			return ResponseEntity.ok(sellers); // Returns a 200 OK with sellers list
		} catch (Exception e) {
			// Handle any exception that occurs during the process
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null); // Returns a 500 Internal Server
																						// Error
		}
	}

	@PatchMapping
	public ResponseEntity<Seller> updateSeller(@RequestHeader("Authorization") String jwt, @RequestBody Seller seller)
			throws Exception {
		Seller profile = sellerService.getSellerProfile(jwt);
		System.out.println("Incoming Seller: " + seller); // Add logging here

		Seller updatedSeller = sellerService.updateSeller(profile.getId(), seller);
		return ResponseEntity.ok(updatedSeller);
	}

	@DeleteMapping
	public ResponseEntity<Void> deleteSeller(@PathVariable Long id) throws Exception {
		sellerService.deleteSeller(id);
		return ResponseEntity.noContent().build();
	}

	@PatchMapping("/updateSellerStatus")
	public ResponseEntity<Seller> updateSellerByAdmin(@RequestHeader("Authorization") String jwt,
			@RequestBody Seller seller) throws Exception {
		Seller updatedSeller = sellerService.updateSeller(seller.getId(), seller);
		return ResponseEntity.ok(updatedSeller);
	}
}
