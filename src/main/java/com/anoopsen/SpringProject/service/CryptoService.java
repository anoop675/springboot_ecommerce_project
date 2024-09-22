package com.anoopsen.SpringProject.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.json.JSONObject;

@Service
public class CryptoService {

    @Value("${crypto.api.url}")
    private String apiUrl;

    @Autowired
    private RestTemplate restTemplate;
    
    public double getEthToInrRate() {// coinGecko's realtime ETH amount API is limited to few requests. which may throw HttpClientErrorException$TooManyRequests
        String url = apiUrl + "/simple/price?ids=ethereum&vs_currencies=inr";
        String response = restTemplate.getForObject(url, String.class);

        JSONObject jsonObject = new JSONObject(response);
        return jsonObject.getJSONObject("ethereum").getDouble("inr");
    }
}