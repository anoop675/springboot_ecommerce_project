package com.anoopsen.SpringProject.dto;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Map;

@Component
@ConfigurationProperties(prefix = "paytm")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaytmDetails {

    private String merchantId;
    private String merchantKey;
    private String website;
    private String industryTypeId;
    private String channelId;
    private String paytmUrl;

    // Map to hold any other dynamic details needed for the request
    private Map<String, String> details;
}
