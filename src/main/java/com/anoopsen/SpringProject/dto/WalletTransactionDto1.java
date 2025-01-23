package com.anoopsen.SpringProject.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WalletTransactionDto1 {
	
	private String infuraProjectId;
	
	@NotBlank(message = "Sender Address is required")
	private String senderAddress;
	
	@NotBlank(message = "Sender Private Key is required")
	private String senderPrivateKey;
	
	private String receiverAddress;
}
