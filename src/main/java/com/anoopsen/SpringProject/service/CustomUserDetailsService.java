package com.anoopsen.SpringProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.anoopsen.SpringProject.model.CustomUserDetails;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService{
	
	@Autowired
	UserRepository userRepo;

	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		Optional<User> user = userRepo.findUserByEmail(email);
		
		user.orElseThrow(
			() -> new UsernameNotFoundException("User not found")
		);
		
		CustomUserDetails userDetails = user.map(u -> new CustomUserDetails(u)).get();

		return userDetails;
	}
}
