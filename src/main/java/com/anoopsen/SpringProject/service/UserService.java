package com.anoopsen.SpringProject.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.anoopsen.SpringProject.config.PasswordEncoderConfig;
import com.anoopsen.SpringProject.model.Cart;
import com.anoopsen.SpringProject.model.Role;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.repository.CartRepo;
import com.anoopsen.SpringProject.repository.RoleRepository;
import com.anoopsen.SpringProject.repository.UserRepository;

@Service
public class UserService {
	
	Logger logger = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	PasswordEncoderConfig pwdEncoder;
	
	@Autowired
	CartService cartService;
	
	public ResponseEntity<String> createUser(User user, String password, List<Role> roles) throws Exception{
		
		try {
			
			if(userRepo.findUserByEmail(user.getEmail()).isPresent()) {
				logger.info("User: "+user.getFirstName()+"with email: "+user.getEmail()+", has already registered");
				throw new Exception();
			}
			
			if(user.getPassword() == null && user.isOauth2User() == false) {
				throw new Exception();
			}
			user.setPassword(pwdEncoder.passwordEncoder().encode(password));
			Role userRole = roleRepo.findById(2).get(); //any registered user has 'User' role
			roles.add(userRole);
			user.setRoles(roles); //setting role
			user.setOauth2User(false);
		
			userRepo.save(user);
			//create an Empty cart simultaneously
			cartService.createEmptyCart(user);
			logger.info("cart created");
			
			logger.info("User: "+user.getFirstName()+"with email: "+user.getEmail()+", is registered successfully");
			return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");

		} catch (Exception e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("An error occurred while creating the user.");
		}
	}

	public boolean verifyPassword(String password){
		String capitalLetterRegex = ".*[A-Z].*";
		String lowerLetterRegex = ".*[a-z].*";
		String numberRegex = ".*[0-9].*";
		String specialCharRegex = ".*[!@#$%].*";
		
		boolean test1 = password.length() >= 8;
		boolean test2 = password.matches(capitalLetterRegex) && password.matches(lowerLetterRegex);
		boolean test3 = password.matches(numberRegex);
		boolean test4 = password.matches(specialCharRegex);
		
		return test1 && test2 && test3 && test4;
	}
	
	public List<User> getAllUsers(){
		return userRepo.findAll();
	}
	
	public void removeUser(int id) {
		userRepo.deleteById(id);
	}

}
