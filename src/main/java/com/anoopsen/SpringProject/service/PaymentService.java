package com.anoopsen.SpringProject.service;
import org.web3j.crypto.Credentials;
import org.web3j.crypto.WalletUtils;
import org.web3j.protocol.Web3j;
import org.web3j.protocol.core.methods.response.TransactionReceipt;
import org.web3j.protocol.http.HttpService;
import org.web3j.tx.ChainId;
import org.web3j.tx.RawTransactionManager;
import org.web3j.tx.Transfer;
import org.web3j.tx.response.Callback;
import org.web3j.tx.response.NoOpProcessor;
import org.web3j.tx.response.QueuingTransactionReceiptProcessor;
import org.web3j.tx.response.TransactionReceiptProcessor;

import javax.crypto.Cipher;
import com.anoopsen.SpringProject.dto.EthTransactionDto;
import com.anoopsen.SpringProject.model.EthWallet;

import jakarta.annotation.PostConstruct;

import java.io.IOException;
import java.util.Base64;
import org.web3j.utils.Convert;

import javax.crypto.spec.SecretKeySpec;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
@PropertySource("classpath:application.properties") 
public class PaymentService {
	/*
    @Autowired
    private RestTemplate restTemplate;

    public String sendEthValue(String EthPaymentApiUrl, double ethAmount) {
    	// Send a POST request with the eth value
        return restTemplate.postForObject(EthPaymentApiUrl, ethAmount, String.class); 
    }*/
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String ALGORITHM = "AES";

    @Value("${service.url}")
    private String infuraUrl;
    
    private Web3j web3j;
    private Callback txReceiptCallback;
    /*
     The NoOpProcessor in the Web3j library is a type of TransactionReceiptProcessor designed to handle transaction receipts
     without polling or waiting for confirmation from the blockchain. In essence, it completes the transaction immediately 
     after the transaction is sent to the network, without checking if it was mined or fully confirmed on the blockchain. 
     */
    private TransactionReceiptProcessor noOpReceiptProcessor;
    private QueuingTransactionReceiptProcessor transactionReceiptProcessor; //needed if we want to receive transaction receipt asynchronously
    /*
    @PostConstruct
    public void init() throws IOException {
        this.web3j = Web3j.build(new HttpService(infuraUrl)); // Connects to Sepolia via Infura
        this.txReceiptCallback = new Callback() {
            @Override
            public void exception(Exception exception) {
                logger.info(exception.getMessage());
            }
            //The accept() method is called when the transaction has been successfully mined and the receipt is available for you to process.
			@Override
			public void accept(TransactionReceipt transactionReceipt) {
		        if (transactionReceipt != null) {
		            // Log transaction details
		        	logger.info("Sender wallet address: "+transactionReceipt.getFrom());
		        	logger.info("Receiver wallet address: "+transactionReceipt.getTo());
		            logger.info("Transaction successful with transaction hash: " + transactionReceipt.getTransactionHash()+" and status: "+transactionReceipt.getStatus());
		            logger.info("Transaction gas fees used: " + transactionReceipt.getGasUsed());
		            logger.info("Transaction is stored in block no."+transactionReceipt.getBlockNumber()+" of block hash:"+transactionReceipt.getBlockHash());
		        } 
		        else {
		            logger.error("Transaction receipt is null or the transaction failed.");
		        }
			}
        };
        logger.info("Connected to Ethereum client version: " + web3j.web3ClientVersion().send().getWeb3ClientVersion());
        this.transactionReceiptProcessor = new QueuingTransactionReceiptProcessor(web3j, txReceiptCallback, 10, 60000);
        this.noOpReceiptProcessor = new NoOpProcessor(web3j);
    }
    
    public Boolean ethExternalTransaction(EthTransactionDto transactionDTO, EthWallet ethWallet) {
    	Web3j parity = Web3j.build(new HttpService(infuraUrl));
        try {
        	String decryptedPwd = this.decrypt(ethWallet.getWalletPwd(), ethWallet.getWalletPwdKey());
            Credentials credentials = WalletUtils.loadCredentials(
                decryptedPwd,
                ethWallet.getWalletJsonFile()
            );

            // Transaction logic here without callback-based receipt processing
            RawTransactionManager transactionManager = new RawTransactionManager(web3j, credentials, ChainId.NONE, noOpReceiptProcessor);
            TransactionReceipt transactionReceipt = Transfer.sendFunds(
                    web3j, credentials, transactionDTO.getSendToAddress(),
                    transactionDTO.getAmountToBeSend(), Convert.Unit.ETHER).send();

            if (transactionReceipt.getTransactionHash() != null) {
                logger.info("Transaction successful with hash: " + transactionReceipt.getTransactionHash());
                return true;
            } else {
                logger.error("Transaction failed or was rejected by the network.");
                return false;
            }

        } catch (Exception e) {
            logger.error("Transaction error: ", e);
            return false;
        }
    }
    public String decrypt(String encryptedText, String secretKey) throws Exception {
        SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(), ALGORITHM);
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        cipher.init(Cipher.DECRYPT_MODE, keySpec);
        byte[] decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(encryptedText));
        
        return new String(decryptedBytes);
    }*/
}
