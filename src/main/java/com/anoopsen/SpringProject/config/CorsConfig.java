package com.anoopsen.SpringProject.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class CorsConfig implements WebMvcConfigurer {
	
	@Override
    public void addCorsMappings(CorsRegistry registry) {
        // This method will allow CORS requests from the external server "ethereum-sepolia-project.onrender.com"
        registry.addMapping("/**")
                .allowedOrigins("https://ethereum-sepolia-project.onrender.com") // Add the external server's URL here
                .allowedMethods("GET", "POST") // Allow only GET and POST methods
                .allowedHeaders("*") // Allow any headers
                .allowCredentials(true); // Allow cookies or credentials if needed
    }

}
