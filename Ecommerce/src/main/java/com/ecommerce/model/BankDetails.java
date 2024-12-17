package com.ecommerce.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class BankDetails {
//	private String accountNumber;
//	private String accountHolderName;
//	private String ifscCode;
	
	@NotBlank(message = "Account number cannot be blank")
    @Pattern(regexp = "^[0-9]{9,18}$", message = "Account number must be between 9 and 18 digits")
    private String accountNumber;

    @NotBlank(message = "Account holder name cannot be blank")
    @Size(min = 3, max = 100, message = "Account holder name must be between 3 and 100 characters")
    private String accountHolderName;

    @NotBlank(message = "IFSC code cannot be blank")
    @Pattern(regexp = "^[A-Z]{4}0[A-Z0-9]{6}$", message = "Invalid IFSC code format")
    private String ifscCode;

    // Getters and Setters (omitted for brevity)

    @Override
    public String toString() {
        return "BankDetails [accountNumber=" + accountNumber + ", accountHolderName=" + accountHolderName + ", ifscCode=" + ifscCode + "]";
    }
}
