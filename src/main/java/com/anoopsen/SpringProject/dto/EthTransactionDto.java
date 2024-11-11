package com.anoopsen.SpringProject.dto;

import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EthTransactionDto {
	
	@NotBlank
    private BigDecimal amountToBeSend;

    @NotBlank
    private Double walletBalance;

    @NotBlank
    private String sendToAddress;

    @NotBlank
    private Double fee;

    @NotBlank
    private Double networkFee;

}
