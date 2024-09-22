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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anoopsen.SpringProject.config.PasswordEncoderConfig;
import com.anoopsen.SpringProject.model.Role;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.repository.RoleRepository;
import com.anoopsen.SpringProject.repository.UserRepository;
import com.anoopsen.SpringProject.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/VITproject")
public class LoginController {
	
	Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	UserService userService;
	
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
			redirectAttributes.addFlashAttribute("errorMessage", "Invalid password entered."); // If there's a conflict (user already exists), show an error message
			return "register";
		}
		
		List<Role> roles = new ArrayList<>();
		
		ResponseEntity<String> response = userService.createUser(user, password, roles);
		
		logger.info("User: "+user.getFirstName()+" with http status: "+response.getStatusCode());
		
		// Handle the response based on the status
	    if (response.getStatusCode() == HttpStatus.CREATED) {
	        request.login(user.getEmail(), password);  // If user is created successfully, log the user in
	        return "redirect:/VITproject/shop";
	    } else if (response.getStatusCode() == HttpStatus.CONFLICT) {
	        model.addAttribute("errorMessage", "User with this email already exists."); // If there's a conflict (user already exists), show an error message
	        return "register";
	    } else {
	        model.addAttribute("errorMessage", "An error occurred during registration. Please try again."); // For other errors, show a generic error message
	        return "register"; 
	    }
	}
	
	//@GetMapping(value="/forgotPassword")
	//public String forgotPassword(Model model) {
		
	//}
	
}
