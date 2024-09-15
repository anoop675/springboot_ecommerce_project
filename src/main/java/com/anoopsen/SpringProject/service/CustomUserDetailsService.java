package com.anoopsen.SpringProject.service;

import java.util.Collection;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.anoopsen.SpringProject.model.CustomUserDetails;
import com.anoopsen.SpringProject.model.Role;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.repository.UserRepository;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepo;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepo.findUserByEmail(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        CustomUserDetails userDetails = new CustomUserDetails(user);
        return userDetails;
    }
}
