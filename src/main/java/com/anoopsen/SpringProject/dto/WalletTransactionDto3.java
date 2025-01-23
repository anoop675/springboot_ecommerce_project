package com.anoopsen.SpringProject.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionDto3 {
	
	private String infuraProjectId;
	
	private String senderPrivateKey;
	
	private String receiverAddress;
}
