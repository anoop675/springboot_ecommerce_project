package com.anoopsen.SpringProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TransactionReceiptDto {
    private String transactionHash;
    
    private String senderAddress;
    
    private String recipientAddress;
    
    private String ethAmount;
}
