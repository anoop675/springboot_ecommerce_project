/*
 	Common attributes used in Google OAuth 2.0:
		sub: Unique identifier for the user.
		name: Full name of the user.
		given_name: The user's first name.
		family_name: The user's last name.
		email: The user's email address.
		picture: URL to the user's profile picture.
		locale: The user's locale.
	When using Google OAuth 2.0 in your application, you typically access these attributes to populate user profiles or for other purposes.
	For example, given_name would be used to retrieve and store the user's first name.
 */
package com.anoopsen.SpringProject.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.anoopsen.SpringProject.model.Role;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.repository.RoleRepository;
import com.anoopsen.SpringProject.repository.UserRepository;
import com.anoopsen.SpringProject.service.CartService;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.DefaultRedirectStrategy;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler{
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	@Autowired
	CartService cartService;
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		OAuth2AuthenticationToken oAuthToken = (OAuth2AuthenticationToken) authentication;
		
		String email = oAuthToken.getPrincipal()
						.getAttributes().get("email").toString();
		
		logger.info("OAuth user email: "+email);
		
		//if user has not registered 
		if(!userRepo.findUserByEmail(email).isPresent()) {
			User user = new User();
			
			logger.info("OAuth user first name: "+oAuthToken.getPrincipal().getAttributes().get("given_name").toString());
			
			user.setFirstName(
				oAuthToken.getPrincipal().getAttributes().get("given_name").toString()
			);
			/*
			user.setPhoneNum(
				oAuthToken.getPrincipal().getAttributes().get("phone_number").toString()
			);*/
			user.setEmail(email);
			user.setOauth2User(true);
			user.setPassword(null);
			
			List<Role> roles = new ArrayList<>();
			roles.add(roleRepo.findById(2).get());
			user.setRoles(roles);
			
			userRepo.save(user);
			
			cartService.createEmptyCart(user);
		}	
		
		redirectStrategy.sendRedirect(request, response, "/VITproject/shop");
	}
}
