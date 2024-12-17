package com.ecommerce.service.impl;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.ecommerce.config.JwtProvider;
import com.ecommerce.domain.UserRole;
import com.ecommerce.model.Cart;
import com.ecommerce.model.Seller;
import com.ecommerce.model.User;
import com.ecommerce.model.VerificationCode;
import com.ecommerce.repository.CartRepository;
import com.ecommerce.repository.SellerRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.repository.VerificationCodeRepository;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.response.SignupRequest;
import com.ecommerce.service.AuthService;
import com.ecommerce.service.EmailService;
import com.ecommerce.util.OtpUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private CartRepository cartRepository;
	@Autowired
	private final JwtProvider jwtProvider;
	@Autowired
	private final PasswordEncoder passwordEncoder;
	@Autowired
	private final VerificationCodeRepository verificationCodeRepository;
	@Autowired
	private final EmailService emailService;
	@Autowired
	private final CustomUserServiceImpl customUserService;
	@Autowired
	private final SellerRepository sellerRepository;

	/**
	 * 
	 * 
	 * */
	@Override
	public String createUser(SignupRequest req) throws Exception {

		VerificationCode verificationCode = verificationCodeRepository.findByEmail(req.getEmail());

		if (verificationCode == null || !verificationCode.getOtp().equals(req.getOtp())) {
			throw new Exception("wrong otp...");
		}

		User user = userRepository.findByEmail(req.getEmail());
		if (user == null) {
			User createdUser = new User();
			createdUser.setEmail(req.getEmail());
			createdUser.setFullName(req.getFullName());
			createdUser.setRole(UserRole.Role_Customer);
			createdUser.setMobile("6565675657");
			createdUser.setPassword(passwordEncoder.encode(req.getOtp()));
			user = userRepository.save(createdUser);
			Cart cart = new Cart();
			cart.setUser(user);
			cartRepository.save(cart);
		}

		List<GrantedAuthority> authorities = new ArrayList<>();
		authorities.add(new SimpleGrantedAuthority(UserRole.Role_Customer.toString()));
		Authentication authentication = new UsernamePasswordAuthenticationToken(req.getEmail(), null, authorities);
		SecurityContextHolder.getContext().setAuthentication(authentication);
		return jwtProvider.generateToken(authentication);
	}

	@Override
	public void sentLoginOtp(String email, UserRole role) throws Exception {
		String SIGNING_PREFIX = "signing_";

		if (email.startsWith(SIGNING_PREFIX)) {
			email = email.substring(SIGNING_PREFIX.length());
			if (role.equals(UserRole.Role_Seller)) {
				Seller seller = sellerRepository.findByEmail(email);
				if (seller == null) {
					throw new Exception("seller not exist");
				}
			} else {
				User user = userRepository.findByEmail(email);

				try {
					if (user == null) {
						throw new Exception("user not exist with provided email");
					}
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}

		}

		VerificationCode isExist = verificationCodeRepository.findByEmail(email);

		if (isExist != null) {
			verificationCodeRepository.delete(isExist);
		}

		String otp = OtpUtil.generateOtp();

		VerificationCode verificationCode = new VerificationCode();
		verificationCode.setOtp(otp);
		verificationCode.setEmail(email);
		verificationCodeRepository.save(verificationCode);

		String subject = "One Time Password for Shop App Login/SignUp";
		String text = "Your Login/SignUp OTP is - " + otp;

		emailService.sendVerificationOtpEmail(email, otp, subject, text);
	}

	@Override
	public AuthResponse signing(LoginRequest req) {
		String username = req.getEmail();
		String otp = req.getOtp();

		Authentication authentication = authenticate(username, otp);
		SecurityContextHolder.getContext().setAuthentication(authentication);

		String token = jwtProvider.generateToken(authentication);
		AuthResponse authResponse = new AuthResponse();
		authResponse.setJwt(token);
		authResponse.setMessage("Login Successfully");

		Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
		String roleName = authorities.isEmpty() ? null : authorities.iterator().next().getAuthority();

		authResponse.setRole(UserRole.valueOf(roleName));
		return authResponse;
	}

	private Authentication authenticate(String username, String otp) {
		UserDetails userDetails = customUserService.loadUserByUsername(username);

		String SELLER_PREFIX = "seller_";
		if (username.startsWith(SELLER_PREFIX)) {
			username = username.substring(SELLER_PREFIX.length());
		}
		if (userDetails == null)
			throw new BadCredentialsException("invalid username or password");

		VerificationCode verificationCode = verificationCodeRepository.findByEmail(username);
		if (verificationCode == null || !verificationCode.getOtp().equals(otp)) {
			System.out.println(otp + "\n" + verificationCode.getOtp());
			throw new BadCredentialsException("wrong otp");
		}
		System.out.println(otp + "\n" + verificationCode.getOtp());
		return new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
	}

}