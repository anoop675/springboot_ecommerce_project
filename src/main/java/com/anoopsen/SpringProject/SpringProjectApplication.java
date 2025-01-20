package com.anoopsen.SpringProject;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.PropertySource;

import com.anoopsen.SpringProject.config.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.exception.TwilioException;

import jakarta.annotation.PostConstruct;


@SpringBootApplication
@PropertySource("classpath:application.properties")  //this will allow spring security to locate the private key mentioned in application.properties for the below @Value
public class SpringProjectApplication {
	
	private static Logger logger = LoggerFactory.getLogger(SpringProjectApplication.class);
	
	/*Twilio website: https://console.twilio.com/*/
	
	@Value("${twilio.account_sid}")
	private String accountSid;
	
	@Value("${twilio.auth_token}")
	private String authToken;
	
	@Value("${twilio.trial_number}")
	private String trialNumber;
		
	//@Autowired
	//TwilioConfig twilioConfig;
	
	//Initializing our twilio configuration to twilio api upon application startup
	@PostConstruct                 //@PostConstruct is used to execute the method initTwilio() as soon as the application starts
	public void initTwilio() {
		TwilioConfig twilioConfig = new TwilioConfig();
		twilioConfig.setAccountSid(accountSid);
		twilioConfig.setAuthToken(authToken);
		twilioConfig.setTrialNumber(trialNumber);
			
		Twilio.init(
			twilioConfig.getAccountSid(),
			twilioConfig.getAuthToken()
		);
				
		logger.info("Twilio configuration is successfully configured with Twilio API");
		logger.info("Twilio account_sid: "+twilioConfig.getAccountSid());
		logger.info("Twilio authentication_token: "+twilioConfig.getAuthToken());
		logger.info("Twilio issued trial number: "+twilioConfig.getTrialNumber());
	}
	
	public static void main(String[] args) throws URISyntaxException {
		//logger.info("Java version: "+System.getProperty("java.version"));
		//logger.info("Class path: "+getClassPath());
			
		SpringApplication.run(SpringProjectApplication.class, args);
	}
	/*	
	public static String getClassPath() throws URISyntaxException {
	    URI codeSourceUri = SpringProjectApplication.class
	                            .getProtectionDomain()
	                            .getCodeSource()
	                            .getLocation()
	                            .toURI();
	    
	    // Convert to a Path (works whether it's a JAR or a directory)
	    Path path = Paths.get(codeSourceUri).getParent(); // Parent directory of the JAR or classes
	    return path.toString();
	}*/
}
