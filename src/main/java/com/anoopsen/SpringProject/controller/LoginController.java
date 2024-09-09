package com.anoopsen.SpringProject.controller;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anoopsen.SpringProject.config.PasswordEncoderConfig;
import com.anoopsen.SpringProject.model.Role;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.repository.RoleRepository;
import com.anoopsen.SpringProject.repository.UserRepository;

import jakarta.servlet.http.HttpServletRequest;

@Controller
@RequestMapping("/VITproject")
public class LoginController {
	
	Logger logger = LoggerFactory.getLogger(LoginController.class);
	
	@Autowired
	PasswordEncoderConfig pwdEncoder;
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@GetMapping(value="/login")
	public String login() {
		return "login";
	}
	
	@GetMapping(value="/register")
	public String registerGet() {
		return "register";
	}
	
	@PostMapping(value="/register")
	public String registerPost(@ModelAttribute("user") User user, HttpServletRequest request) throws Exception {
		
		logger.info(
				"firstname: "+user.getFirstName()
				+", lastname: "+user.getLastName()
				+", email: "+user.getEmail()
				+", password: "+user.getPassword()
			);
		
		String password = user.getPassword();
		user.setPassword(pwdEncoder.passwordEncoder().encode(password));
		List<Role> roles = new ArrayList<>();
		roles.add(roleRepo.findById(2).get());
		user.setRoles(roles);
		userRepo.save(user);
		
		request.login(user.getEmail(), password);
		return "redirect:/VITproject/home";
	}
	
}
