package com.anoopsen.SpringProject.service;


import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Random;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.anoopsen.SpringProject.config.TwilioConfig;
import com.anoopsen.SpringProject.model.OtpStatus;
import com.anoopsen.SpringProject.dto.PasswordResetRequestDto;
import com.anoopsen.SpringProject.dto.PasswordResetResponseDto;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;


@Service
public class TwilioOtpService {
	
	@Autowired
	TwilioConfig twilioConfig;
	
	private Map<String, String> otpMap = new HashMap<>();   //Not using a DB to store otp, using a hashmap. In real-application otp is stored which is mapped with the user's username

	final Long expirationTimeInMillis = Long.valueOf(1000 * 60 * 5);   //expirationTime is 5 minutes
	
	Long startTime = null;
	Long endTime = null;
	
	public PasswordResetResponseDto sendOtp(PasswordResetRequestDto request) {
		//This code will define the OTP sending logic
		
		PasswordResetResponseDto response = null;
		
		try {
			
			PhoneNumber sender = new PhoneNumber(twilioConfig.getTrialNumber());
			PhoneNumber receiver = new PhoneNumber(request.getMyPhoneNumber());
			
			String otp = generateOtp();
			String otpMessage = "Hi "+request.getUserName()+", Your OTP is "+otp+". Use this OTP to complete your password reset request. This OTP expires in "+(expirationTimeInMillis/(1000*60))+" minutes. Thank you.";
			
			//logic to send OTP to receiptant's phone number via Twilio API
			//-------------------------------------------------------------
			Message message = Message.creator(
								receiver,
								sender,
								otpMessage
							  )
							  .create();
			//-------------------------------------------------------------
			
			//saving otp for user in a hash-map
			otpMap.put(request.getUserName(), otp);
			
			startTime = System.currentTimeMillis();  
			
			response = new PasswordResetResponseDto(
							OtpStatus.DELIVERED,
							"Otp sent successfully! Check SMS for the number: "+receiver.toString());
			
			startTime = System.currentTimeMillis();
		}
		catch(Exception e) {
			response = new PasswordResetResponseDto(
						OtpStatus.FAILED,
						"Otp could not be sent due to this issue: "+e.getMessage());
		}
		
		return response;
	}
	
	public ResponseEntity<String> validateOtp(String enteredOtp, String userName){
		//This code will validate the OTP entered by user
		
		endTime = System.currentTimeMillis();
		
		if((endTime - startTime) >= expirationTimeInMillis){
			otpMap.remove(userName);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Your OTP is now expired, Try requesting a new OTP");
		}
		
		else if(enteredOtp.equals(otpMap.get(userName))) {
			otpMap.remove(userName); //removing userName's OTP from the hash map
			return ResponseEntity.status(HttpStatus.OK).body("Valid OTP entered!");
		}	
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Invalid OTP entered!");
	}
	
	
	public String generateOtp() {
		String pattern = "00000";  //5 digit OTP
		Integer bound = 99999;
		return new DecimalFormat(pattern).format(new Random().nextInt(bound));
		
	}
}
