package com.anoopsen.SpringProject.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import lombok.Data;

@Configuration
@ConfigurationProperties(prefix = "twilio")
@Data
public class TwilioConfig {

	private String accountSid;
	
	private String authToken;
	
	private String trialNumber;
}

/*
	NOTE: add dependency in pom.xml for @ConfigurationProperties
	
	<dependency>
		<groupId>org.springframework.boot</groupId>
		<artifactId>spring-boot-configuration-processor</artifactId>
		<optional>true</optional>
	</dependency>
*/