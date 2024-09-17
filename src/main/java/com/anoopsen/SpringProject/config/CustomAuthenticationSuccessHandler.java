package com.anoopsen.SpringProject.config;

import java.io.IOException;

import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) 
    		throws IOException, ServletException {
        boolean isAdmin = authentication.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN"));  // If the user has the ADMIN role
        
        if (isAdmin) {
            response.sendRedirect("/VITproject/admin"); // Redirect admin to /VITproject/admin
        } else {
            response.sendRedirect("/VITproject/shop"); // Redirect normal users to /VITproject/home
        }
    }
}
