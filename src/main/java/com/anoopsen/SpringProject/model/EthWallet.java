package com.anoopsen.SpringProject.model;

import java.math.BigDecimal;

import com.anoopsen.SpringProject.dto.EthTransactionDto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EthWallet {
	
    private String walletPwd;
    
    private String walletPwdKey;
    
    private String walletJsonFile;
}
