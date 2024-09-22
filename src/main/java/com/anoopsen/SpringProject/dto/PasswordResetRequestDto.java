package com.anoopsen.SpringProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PasswordResetRequestDto {

	private String myPhoneNumber;   //This is the phone number that will receive the otp  [eg: +91 96XXXXXX03]
	
	private String userName;
	
	private String otp;
}

