package com.anoopsen.SpringProject.config;

import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
public class CryptoConfig {
	
	@Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
