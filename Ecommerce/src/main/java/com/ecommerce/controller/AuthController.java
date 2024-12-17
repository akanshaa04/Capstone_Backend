package com.ecommerce.controller;
 
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
 
import com.ecommerce.domain.UserRole;
import com.ecommerce.model.User;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.request.LoginOtpRequest;
import com.ecommerce.request.LoginRequest;
import com.ecommerce.response.ApiResponse;
import com.ecommerce.response.AuthResponse;
import com.ecommerce.response.SignupRequest;
import com.ecommerce.service.AuthService;
 
@RestController
@RequestMapping("/auth")
public class AuthController {
//	@Autowired
//	private UserRepository userRepository;
	@Autowired
    private AuthService authService;
	
	@PostMapping("/signup")
	public ResponseEntity<AuthResponse> createUserHandler(@RequestBody SignupRequest req) throws Exception{
		String jwt = authService.createUser(req);
		AuthResponse res = new AuthResponse();
		res.setJwt(jwt);
		res.setMessage("Register Success");
		res.setRole(UserRole.Role_Customer);
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/signup/otp")
	public ResponseEntity<ApiResponse> sentOtpHandler(@RequestBody LoginOtpRequest req) throws Exception{
		authService.sentLoginOtp(req.getEmail(),req.getRole());
		ApiResponse res = new ApiResponse();
		res.setMessage("otp sent successfully");
		return ResponseEntity.ok(res);
	}
	
	@PostMapping("/login/otp")
	public ResponseEntity<AuthResponse> loginHandler(@RequestBody LoginRequest req) throws Exception{
		AuthResponse authResponse = authService.signing(req);
		
		return ResponseEntity.ok(authResponse);
	}
}