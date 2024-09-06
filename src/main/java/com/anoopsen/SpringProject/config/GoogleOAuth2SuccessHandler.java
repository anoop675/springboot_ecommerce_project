package com.anoopsen.SpringProject.config;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import com.anoopsen.SpringProject.model.Role;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.repository.RoleRepository;
import com.anoopsen.SpringProject.repository.UserRepository;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.web.RedirectStrategy;
import org.springframework.security.web.DefaultRedirectStrategy;

@Component
public class GoogleOAuth2SuccessHandler implements AuthenticationSuccessHandler{
	
	@Autowired
	UserRepository userRepo;
	
	@Autowired
	RoleRepository roleRepo;
	
	private RedirectStrategy redirectStrategy = new DefaultRedirectStrategy();

	@Override
	public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
			Authentication authentication) throws IOException, ServletException {
		
		OAuth2AuthenticationToken oAuthToken = (OAuth2AuthenticationToken) authentication;
		
		String email = oAuthToken.getPrincipal()
						.getAttributes().get("email").toString();
		
		//if user has not registered 
		if(!userRepo.findUserByEmail(email).isPresent()) {
			User user = new User();
			
			user.setFirstName(
				oAuthToken.getPrincipal().getAttributes().get("given_name").toString()
			);
			user.setLastName(
				oAuthToken.getPrincipal().getAttributes().get("family_name").toString()
			);
			user.setEmail(email);
			
			List<Role> roles = new ArrayList<>();
			roles.add(roleRepo.findById(2).get());
			user.setRoles(roles);
			
			userRepo.save(user);
		}	
		
		redirectStrategy.sendRedirect(request, response, "/VITproject/home");
	}
}
