package com.anoopsen.SpringProject;

import java.io.File;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringProjectApplication {
	
	private static Logger logger = LoggerFactory.getLogger(SpringProjectApplication.class);
		
	
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
