package com.anoopsen.SpringProject.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anoopsen.SpringProject.config.PasswordEncoderConfig;
import com.anoopsen.SpringProject.dto.PasswordResetDto;
import com.anoopsen.SpringProject.dto.PasswordResetRequestDto;
import com.anoopsen.SpringProject.dto.PasswordResetResponseDto;
import com.anoopsen.SpringProject.model.OtpStatus;
import com.anoopsen.SpringProject.model.Role;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.repository.RoleRepository;
import com.anoopsen.SpringProject.repository.UserRepository;
import com.anoopsen.SpringProject.service.TwilioOtpService;
import com.anoopsen.SpringProject.service.UserService;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/VITproject")
public class LoginController {
	
	Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	UserService userService;
	
	@Autowired
	TwilioOtpService twilioOtpService;
	
	@GetMapping(value="/login")
	public String login() {
		return "login";
	}
	
	@GetMapping(value="/register")
	public String registerGet() {
		return "register";
	}
	
	@PostMapping(value="/register")
	public String registerPost(@ModelAttribute("user") User user, HttpServletRequest request, 
			RedirectAttributes redirectAttributes, Model model) throws Exception {
		
		logger.info(
				"firstname: "+user.getFirstName()
				+", phoneNum: "+user.getPhoneNum()
				+", email: "+user.getEmail()
				+", password: "+user.getPassword()
			);
		
		String password = user.getPassword();
		if(!userService.verifyPassword(password)) {
			logger.info("Invalid password entered.");
			model.addAttribute("errorMessage", "Invalid password entered."); // If there's a conflict (user already exists), show an error message
			return "register";
		}
		
		List<Role> roles = new ArrayList<>();
		
		ResponseEntity<String> response = userService.createUser(user, password, roles);
		
		logger.info("User: "+user.getFirstName()+" with http status: "+response.getStatusCode());
		
		// Handle the response based on the status
	    if (response.getStatusCode() == HttpStatus.CREATED) {
	        request.login(user.getEmail(), password);  // If user is created successfully, log the user in
	        return "redirect:/VITproject/shop";
	    }
	    else if (response.getStatusCode() == HttpStatus.CONFLICT) {
	        model.addAttribute("errorMessage", "User with this email already exists."); // If there's a conflict (user already exists), show an error message
	        model.addAttribute("user", user);
	        return "register";
	    } 
	    else {
	        model.addAttribute("errorMessage", "An error occurred during registration. Please try again."); // For other errors, show a generic error message
	        return "register"; 
	    }
	}
	
	@GetMapping(value="/forgotPassword")
	public String forgotPassword(Model model) {
		model.addAttribute("message", "Please provide your registered email id");
		return "forgotPassword";
	}
	/*
	@GetMapping(value="/otp")
	public String getOtpPage(Model model) {
		model.addAttribute("dto", new PasswordResetRequestDto());
		return "otp";
	}*/
	
	@PostMapping(value="/forgotPassword")
	public String forgotPassword(@RequestParam String email, Model model) {
	    User user = userService.getUser(email);
	    String thisPage = "";
	    if (user == null) {
	        model.addAttribute("errorMessage", "User is not found");
	        thisPage = "forgotPassword";
	    } 
	    else {
	    	PasswordResetResponseDto dto = twilioOtpService.sendOtp(email);
	    	if(dto.getStatus().equals(OtpStatus.DELIVERED)) {
		    	String formattedPhoneNumber = "+91 " + user.getPhoneNum().substring(0, 2) + "*****" + user.getPhoneNum().substring(7);
		    	model.addAttribute("message", "An OTP has been sent to your registered number " + formattedPhoneNumber + " via SMS");
		    	model.addAttribute("email", email);  // Send email for later use in OTP verification
		    	model.addAttribute("dto2", new PasswordResetRequestDto(email, ""));
		        thisPage = "otp";  // Redirect to OTP verification page

	    	}
	    	else if(dto.getStatus().equals(OtpStatus.FAILED)) {
	    		model.addAttribute("errorMessage", "Failed to send OTP to registered number");
	    		 thisPage = "forgotPassword"; 
	    	}
	    }
	    return thisPage;
	}
	
	
	@PostMapping("/verifyOTP") //GET is not supported (i.e if we type /VITproject/verifyOTP). this makes it secure from unauthorized access
	public String verifyOtp(@ModelAttribute("dto2") PasswordResetRequestDto prrd, Model model) {
	    String email = prrd.getEmail();
	    String otp = prrd.getOtp();

	    logger.info("verifyOtp() in controller called, received email: " + email + " and otp: " + otp);

	    ResponseEntity<String> status = twilioOtpService.validateOtp(otp, userService.getUser(email).getFirstName());

	    if (status.getStatusCode().equals(HttpStatus.OK)) {
	        logger.info("Entered otp: " + otp + " is valid");
	        PasswordResetDto dto3 = new PasswordResetDto();
	        dto3.setEmail(email);
	        dto3.setNewPassword("");
	        dto3.setConfirmPassword("");
	        model.addAttribute("dto3", dto3);
	        return "resetPassword";
	    } else {
	        logger.info("Entered otp: " + otp + " is invalid");
	        model.addAttribute("errorMessage", "Invalid OTP entered, Please try again");
	        return "otp";
	    }
	}
	
	@PostMapping(value="/resetPassword")   //GET is not supported (i.e if we type /VITproject/resetPassword). this makes it secure from unauthorized access
	public String resetPassword(@ModelAttribute("dto3") PasswordResetDto prd, Model model) {
		String email = prd.getEmail();
	    String newPwd = prd.getNewPassword();
	    String confirmPwd = prd.getConfirmPassword();

	    if (newPwd.equals(confirmPwd) && userService.verifyPassword(newPwd)) {
	        logger.info("passwords are a match");
	        userService.resetPassword(email, newPwd);
	        model.addAttribute("message", "Password reset successful! You may now login");
	        return "login";
	    }
	    else {
	        model.addAttribute("errorMessage", "Invalid password entered, Please try again");
	        return "resetPassword";
	    }
	}
}
