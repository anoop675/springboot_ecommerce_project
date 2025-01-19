package com.anoopsen.SpringProject.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import com.anoopsen.SpringProject.config.PasswordEncoderConfig;
import com.anoopsen.SpringProject.model.Cart;
import com.anoopsen.SpringProject.model.ProductRating;
import com.anoopsen.SpringProject.model.Role;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.repository.CartRepo;
import com.anoopsen.SpringProject.repository.RatingRepository;
import com.anoopsen.SpringProject.repository.RoleRepository;
import com.anoopsen.SpringProject.repository.UserRepository;

import jakarta.transaction.Transactional;

@Service
@Transactional
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
	
	@Autowired
	RatingRepository ratingRepo;
	
	
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
			/*
			create an Empty cart simultaneously (when Cart had Products)
			cartService.createEmptyCart(user);
			*/
			cartService.createEmptyCart(user);
			
			
			logger.info("cart created");
			
			logger.info("User: "+user.getFirstName()+"with email: "+user.getEmail()+", is registered successfully");
			return ResponseEntity.status(HttpStatus.CREATED).body("User created successfully.");

		} catch (Exception e) {
			logger.info(e.getMessage());
			e.printStackTrace();
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
		
		logger.info("Password: "+password+" is valid?: "+String.valueOf(test1 && test2 && test3 && test4));
		
		return test1 && test2 && test3 && test4;
	}
	
	public User getAuthenticatedUser() throws Exception {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
	    
	    if (authentication != null) {
	        Object principal = authentication.getPrincipal();
	        
	        // Check if principal is an instance of OidcUser (OAuth2/OpenID Connect user)
	        if (principal instanceof OidcUser) {
	            OidcUser oidcUser = (OidcUser) principal;
	            String email = oidcUser.getEmail();  // Or use another identifier like 'sub'
	            
	            // Fetch user from your database (you might use email or other identifiers)
	            User user = userRepo.findUserByEmail(email).orElseThrow(() -> new Exception("User not found"));
	            return user;
	        }
	        
	        // Check if principal is an instance of OAuth2User (for general OAuth2 authentication)
	        else if (principal instanceof OAuth2User) {
	            OAuth2User oauth2User = (OAuth2User) principal;
	            String email = oauth2User.getAttribute("email");  // Replace with appropriate attribute
	            
	            // Fetch user from your database
	            User user = userRepo.findUserByEmail(email).orElseThrow(() -> new Exception("User not found"));
	            return user;
	        }
	        
	        // If it's a simple user (e.g., in-memory authentication)
	        else if (principal instanceof User) {
	            return (User) principal;
	        }
	    }
	    return null; // Return null if not authenticated
	}
	
	public String getAuthenticatedUserFirstName() {
		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal(); //get principal (authenticated user)
		String firstName = "";
	    // Check if the user is authenticated via OAuth2
	    if (principal instanceof DefaultOidcUser) {
	        DefaultOidcUser oidcUser = (DefaultOidcUser) principal;
	        firstName = oidcUser.getGivenName(); // "given_name" is attribute provided by Google Console
	    } 
	    // Check if the user is authenticated as a traditional user
	    else if (principal instanceof User) {
	        User user = (User) principal;
	        firstName = user.getFirstName();
	    }
	    return firstName;
	}
	
	public List<User> getAllUsers(){
		return userRepo.findAll();
	}
	
	public User getUser(int id) {
		return userRepo.findById(id)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
	
	public void addUser(User user) {
		userRepo.save(user);
	}
	public User getUser(String email) {
		return userRepo.findUserByEmail(email)
				.orElseThrow(() -> new UsernameNotFoundException("User not found"));
	}
	
	public void removeUser(int id) {
		cartService.deleteCartById(this.getUser(id).getCart().getId());
		ratingRepo.deleteByUserId(id);
		userRepo.deleteById(id);
	}
	
	public void resetPassword(String email, String newPwd){
		User user = getUser(email);
		user.setPassword(pwdEncoder.passwordEncoder().encode(newPwd));
		userRepo.save(user); //JPA updates existing user's pwd
	}

}
