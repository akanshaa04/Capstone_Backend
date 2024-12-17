package com.ecommerce.service.impl;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.ecommerce.Exception.SellerException;
import com.ecommerce.config.JwtProvider;
import com.ecommerce.model.Address;
import com.ecommerce.model.User;
import com.ecommerce.repository.AddressRepository;
import com.ecommerce.repository.UserRepository;
import com.ecommerce.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService{
	
	@Autowired
	private final UserRepository userRepository;
	@Autowired
	private final AddressRepository addressRepository;
	@Autowired
	private final JwtProvider jwtProvider;
	
	
   @Override
    public List<User> findAllUsers() {
        return userRepository.findAll();  // Fetch all users
    }
   
	@Override
	public User findUserByJwtToken(String jwt) throws Exception {
		String email = jwtProvider.getEmailFromJwtToken(jwt);
		User user = this.findUserByEmail(email);
		
		return user;
	}

	@Override
	public User findUserByEmail(String email) throws Exception {
		User user = userRepository.findByEmail(email);
		
		if(user==null) {
			throw new Exception("user not found with email - " + email);
		}
		return user;
	}

	@Override
	public User updateUser(Long id, User user) throws Exception {
	    // Fetch the existing user from the database
	    User existingUser = this.getUserById(id);
        System.out.println("alrady "+ existingUser.getMobile());
        System.out.println("coming" + user.getMobile());
	    // Update the mobile number if provided
	    if (user.getMobile() != null) {
	        existingUser.setMobile(user.getMobile());
	    }

        System.out.println("upated "+ existingUser.getMobile());

	    // Update address if provided
	    if (user.getAddresses() != null && !user.getAddresses().isEmpty()) {
	        // Assuming the first address to be updated
	        Address existingAddress = existingUser.getAddresses().stream()
	                                             .findFirst()
	                                             .orElse(new Address());

	        Address userAddress = user.getAddresses().iterator().next();

	        if (userAddress.getAddress() != null) {
	            existingAddress.setAddress(userAddress.getAddress());
	        }
	        if (userAddress.getLocality() != null) {
	            existingAddress.setLocality(userAddress.getLocality());
	        }
	        if (userAddress.getMobile() != null) {
	            existingAddress.setMobile(userAddress.getMobile());
	        }
	        if (userAddress.getCity() != null) {
	            existingAddress.setCity(userAddress.getCity());
	        }
	        if (userAddress.getState() != null) {
	            existingAddress.setState(userAddress.getState());
	        }
	        if (userAddress.getPinCode() != null) {
	            existingAddress.setPinCode(userAddress.getPinCode());
	        }
	        if (userAddress.getName() != null) {
	            existingAddress.setName(userAddress.getName());
	        }
	        existingUser.getAddresses().add(existingAddress);
	    }

	   
	    return userRepository.save(existingUser);
	}

	private User getUserById(Long id) throws SellerException {
		return userRepository.findById(id).
                orElseThrow(()-> new SellerException("User not found with id - " + id));
 
	}

	

    @Override
    public User addAddress(Long userId, Address address) throws Exception {
        User user = getUserById(userId);

        // Save the address to the database first
        Address savedAddress = addressRepository.save(address);

        // Add the saved address to the user's address set
        user.getAddresses().add(savedAddress);
        return userRepository.save(user);
    }
    
    
    @Override
	public void updateUserWallet(Long userId, Integer walletAmount) throws Exception {
		// TODO Auto-generated method stub
		User user = userRepository.findById(userId).get();
		if(user != null) {
			user.setWallet(walletAmount.longValue());
			userRepository.save(user);
		}
	}
    
    @Override
	public List<User> walletBalance() {
		List<User> li = userRepository.findAll();

		List<User> result = li.stream().filter(e -> e.getEmail().toLowerCase().endsWith("@incedoinc.com")).collect(Collectors.toList());
		return result;
	}
	
}
