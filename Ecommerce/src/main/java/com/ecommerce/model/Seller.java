package com.ecommerce.model;

import java.time.LocalDateTime;
import java.util.List;

import com.ecommerce.domain.UserRole;
import com.ecommerce.domain.AccountStatus;
import com.fasterxml.jackson.annotation.JsonIgnore;

import jakarta.persistence.Column;
import jakarta.persistence.Embedded;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EqualsAndHashCode
public class Seller {

	@Override
	public String toString() {
		return "Seller [id=" + id + ", sellerName=" + sellerName + ", mobile=" + mobile + ", email=" + email
				+ ", password=" + password + ", businessDetails=" + businessDetails + ", bankDetails=" + bankDetails
				+ ", pickupAddress=" + pickupAddress + ", GSTIN=" + GSTIN + ", role=" + role + ", isEmailVerfied="
				+ isEmailVerfied + ", accountStatus=" + accountStatus + "]";
	}

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	
	private String sellerName;
	
	private String mobile;
	
	@Column(unique = true, nullable = false)
	private String email;
	
	private String password;
	
	@Embedded
	private BusinessDetails businessDetails = new BusinessDetails();
	
	@Embedded
	private BankDetails bankDetails = new BankDetails();
	
	@OneToOne
	private Address pickupAddress = new Address();
	
	private String GSTIN;
	
	private UserRole role = UserRole.Role_Seller;
	
	private boolean isEmailVerfied = false;
	
	private AccountStatus accountStatus = AccountStatus.Pending_Verification;
}
