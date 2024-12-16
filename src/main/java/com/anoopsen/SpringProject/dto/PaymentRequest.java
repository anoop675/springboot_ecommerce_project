package com.anoopsen.SpringProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentRequest {
	
    private String orderId;
    
    private String customerId;
    
    private String amount;
    
    private String email;
    
    private String mobile;
}
