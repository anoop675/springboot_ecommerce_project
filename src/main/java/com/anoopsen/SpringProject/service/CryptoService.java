package com.anoopsen.SpringProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class CryptoService {
	
	Logger logger = LoggerFactory.getLogger(getClass());

    @Value("${crypto.api.url}")
    private String apiUrl;
    
   //@Value("${crypto.user.agent}")
   // private String userAgent;

    @Autowired
    private RestTemplate restTemplate;

    public double getEthToInrRate() {
        //String url = apiUrl + "/simple/price?ids=ethereum&vs_currencies=inr";
    	String url = apiUrl + "/data/price?fsym=ETH&tsyms=BTC,INR";
    	
    	try {
            // Create headers
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            // Create HTTP entity
            HttpEntity<String> entity = new HttpEntity<>(headers);

            // Make the HTTP GET request with headers
            ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

            // Parse JSON response
            JSONObject jsonObject = new JSONObject(response.getBody());
            return jsonObject.getDouble("INR");

        } catch (HttpClientErrorException.TooManyRequests e) {
            System.err.println("Rate limit exceeded. Please try again later.");
            return -1; // Fallback value
        } catch (Exception e) {
            System.err.println("An error occurred: " + e.getMessage());
            return -1; // Fallback value
        }
    	
    }
}
