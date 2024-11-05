package com.anoopsen.SpringProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaymentService { //TODO: ---

    @Autowired
    private RestTemplate restTemplate;

    public String sendEthValue(String EthPaymentApiUrl, double ethAmount) {
    	// Send a POST request with the eth value
        return restTemplate.postForObject(EthPaymentApiUrl, ethAmount, String.class); 
    }
}
