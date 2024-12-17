package com.ecommerce.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
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
public class Address {

//	@Id
//	@GeneratedValue(strategy = GenerationType.AUTO)
//	private Long id;
//	
//	private String name;
//	
//	private String locality;
//	
//	private String address;
//	
//	private String city;
//	
//	private String state;
//	
//	private String pinCode;
//	
//	private String mobile;
//
//	@Override
//	public String toString() {
//		return "Address [id=" + id + ", name=" + name + ", locality=" + locality + ", address=" + address + ", city="
//				+ city + ", state=" + state + ", pinCode=" + pinCode + ", mobile=" + mobile + "]";
//	}
	
	
	 	@Id
	    @GeneratedValue(strategy = GenerationType.AUTO)
	    private Long id;

	    @NotBlank(message = "Name cannot be blank")
	    private String name;

	    @NotBlank(message = "Locality cannot be blank")
	    private String locality;

	    @NotBlank(message = "Address cannot be blank")
	    private String address;

	    @NotBlank(message = "City cannot be blank")
	    private String city;

	    @NotBlank(message = "State cannot be blank")
	    private String state;

	    @Pattern(regexp = "^[0-9]{6}$", message = "Pin code must be exactly 6 digits")
	    private String pinCode;

	    @Pattern(regexp = "^[0-9]{10}$", message = "Mobile number must be exactly 10 digits")
	    private String mobile;

	    // Getters and Setters

	    @Override
	    public String toString() {
	        return "Address [id=" + id + ", name=" + name + ", locality=" + locality + ", address=" + address + ", city=" 
	                + city + ", state=" + state + ", pinCode=" + pinCode + ", mobile=" + mobile + "]";
	    }
	 
}
