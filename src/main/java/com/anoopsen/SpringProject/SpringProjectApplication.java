package com.anoopsen.SpringProject;

import java.io.File;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.anoopsen.SpringProject.config.TwilioConfig;
import com.twilio.Twilio;
import com.twilio.exception.TwilioException;

import jakarta.annotation.PostConstruct;

@SpringBootApplication
public class SpringProjectApplication {
	
	private static Logger logger = LoggerFactory.getLogger(SpringProjectApplication.class);
		
	@Autowired
	TwilioConfig twilioConfig;
	
	//Initializing our twilio configuration to twilio api upon application startup
	@PostConstruct                 //@PostConstruct is used to execute the method initTwilio() as soon as the application starts
	public void initTwilio() {
			
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
		logger.info("Java version: "+System.getProperty("java.version"));
		logger.info("Class path: "+getClassPath());
			
		SpringApplication.run(SpringProjectApplication.class, args);
	}
		
	public static String getClassPath() throws URISyntaxException {
		File path = new File(SpringProjectApplication.class.getProtectionDomain()
						.getCodeSource().getLocation().toURI());
			
		return path.getPath();
	}
}
