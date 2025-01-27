package com.anoopsen.SpringProject.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.anoopsen.SpringProject.repository.CartRepo;
import com.anoopsen.SpringProject.service.CartService;
import com.anoopsen.SpringProject.service.CryptoService;
import com.anoopsen.SpringProject.service.PaymentService;
//import com.paytm.pg.merchant.PaytmChecksum;

import jakarta.servlet.http.HttpServletRequest;

import com.anoopsen.SpringProject.dto.PaytmDetails;
import com.anoopsen.SpringProject.dto.TransactionReceiptDto;
import com.anoopsen.SpringProject.dto.WalletTransactionDto1;
import com.anoopsen.SpringProject.model.Cart;
import com.anoopsen.SpringProject.model.User;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Controller
@RequestMapping(value="/VITproject")
public class PaymentController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	final static String receiver_metamask_walletAddress = "0xB51C492e6dE5a858785fccAFa46F1DeF070a1b65";
	
	final static String infuraProjectId = "dde4a14c79c34a43b17ebc32f22ce6a4";

	
	@Value("${ethpaymentapi.url}")
	private String EthPaymentApiUrl;
	
	@Autowired
	CartRepo cartRepo;

	@Autowired
	CartService cartService;
	
	@Autowired
	CryptoService cryptoService;
	
	@Autowired
	PaymentService paymentService;
	
	
	@PostMapping(value = "/paytm/make-payment")
	public String paymentPage(RedirectAttributes attr) {
		//TODO: integrate paytm or paypal payments gateway
		attr.addFlashAttribute("error", "Sorry this payment method is currently unavailable. Please try a different payment method.");
		return "redirect:/VITproject/checkout";
	}
	
	public ResponseEntity<String> connectToInfura() {
		
		try {
	        // Build the request
	        JSONObject payload = new JSONObject();
	        payload.put("infura_project_id", infuraProjectId);
	        
	        HttpHeaders headers = new HttpHeaders();
	        headers.setContentType(MediaType.APPLICATION_JSON);
	        
	        HttpEntity<String> requestEntity = new HttpEntity<>(payload.toString(), headers);
	        
	        // Send request using RestTemplate
	        RestTemplate restTemplate = new RestTemplate();
	        String url = EthPaymentApiUrl + "/connect";
	        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
	        
	        if(response.getStatusCode() == HttpStatus.OK) {
	        	return response;
	        } 
	        else {
	        	logger.info("Unable to connect to API with error: {}", response.getBody());
	        	return new ResponseEntity<>("Internal Server Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
	        } 
		}
		catch(Exception e) {
			logger.info("Unable to connect to API with error: {}", e.getMessage());
			return new ResponseEntity<>("Internal Server Error occurred", HttpStatus.INTERNAL_SERVER_ERROR);
		}   
	}
	
	@PostMapping(value = "/eth-payment")
	public String showEthPaymentPage(@ModelAttribute WalletTransactionDto1 walletTransactionDto1, Model model, @RequestParam("ethAmount") String ethAmount, RedirectAttributes attr) {
		ResponseEntity<String> response = this.connectToInfura();
		
	    if (response.getStatusCode() == HttpStatus.OK) { //if response is 200 OK, direct to Eth payment page
	    	JSONObject jsonResponse = new JSONObject(response.getBody());
	        logger.info("Connection successful: {}", jsonResponse.get("message"));
	        model.addAttribute("walletTransactionDto1", new WalletTransactionDto1());
	    	model.addAttribute("ethAmount", ethAmount);
	    	//model.addAttribute("receiverAddress", receiver_metamask_walletAddress);
	    	return "ethPayment";
	    } 
	    else {
	    	logger.info("Unable to connect to API with error: {}", response.getBody());
	        attr.addFlashAttribute("error", "Redirection to payment failed with status code: " + response.getStatusCode());
	        return "redirect:/VITproject/checkout";
	    }
	}
	
	@PostMapping(value="/create-wallet")
	public String createWallet(@ModelAttribute WalletTransactionDto1 walletTransactionDto1, Model model, @RequestParam("ethAmount") String ethAmount) {
		ResponseEntity<String> connectionResponse = this.connectToInfura();
		
	    if (connectionResponse.getStatusCode() == HttpStatus.OK) { //if response is 200 OK, direct to Eth payment page
	        String senderWalletAddress = "";
	        String senderPrivateKey = "";
	        
	        try {
	        	/*
		        // Build the request
		        JSONObject payload = new JSONObject();
		        payload.put("infura_project_id", infuraProjectId);
		        
		        HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.APPLICATION_JSON);
		        
		        HttpEntity<String> requestEntity = new HttpEntity<>(payload.toString(), headers);
		        
		        // Send request using RestTemplate
		        RestTemplate restTemplate = new RestTemplate();
		        String url = EthPaymentApiUrl + "/get-wallet";
		        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		        	
		        JSONObject jsonResponse = new JSONObject(response.getBody());
		        senderWalletAddress = jsonResponse.optString("sender_address", "");
		        senderPrivateKey = jsonResponse.optString("sender_private_key", "");
		        
		        logger.info("Connection successful and received wallet address");*/
	        	
	        	//Generating wallet address locally
	        	List<String> credentials = cryptoService.getWalletCredentials();
	        	senderWalletAddress = credentials.get(0);
	        	senderPrivateKey = credentials.get(1);
	        	
		    	model.addAttribute("senderWalletAddress", senderWalletAddress);
		    	model.addAttribute("senderPrivateKey", senderPrivateKey);
	        }
	        catch(Exception e) {
	        	logger.info("Unable to get wallet from API with error: {}", e.getMessage());
		    	model.addAttribute("error", "Wallet generation failed: " + e.getStackTrace());
	        }
	    } 
	    else {
	    	logger.info("Unable to connect to API with error: {}", connectionResponse.getStatusCode());
	    	model.addAttribute("error", "Process failed due to connection issue with status code: " + connectionResponse.getStatusCode());
	    }
	   	model.addAttribute("walletTransactionDto1", new WalletTransactionDto1());
    	model.addAttribute("ethAmount", ethAmount);
    	//model.addAttribute("receiverAddress", receiver_metamask_walletAddress);
		return "ethPayment";
	}
	
	@PostMapping(value="/check-wallet-balance")
	public String checkWalletBalance(@ModelAttribute WalletTransactionDto1 walletTransactionDto1, Model model, @RequestParam("senderWalletAddress") String senderAddress, @RequestParam("ethAmount") String ethAmount) {
		ResponseEntity<String> connectionResponse = this.connectToInfura();
		
	    if (connectionResponse.getStatusCode() == HttpStatus.OK) { //if response is 200 OK, direct to Eth payment page
	        String senderWalletBalance = "";
	        String trimmedSenderAddress = senderAddress.trim(); //remove leading and trailing whitespaces
	        try {
		        // Build the request
		        JSONObject payload = new JSONObject();
		        payload.put("infura_project_id", infuraProjectId);
		        payload.put("sender_address", trimmedSenderAddress);
		        
		        HttpHeaders headers = new HttpHeaders();
		        headers.setContentType(MediaType.APPLICATION_JSON);
		        
		        HttpEntity<String> requestEntity = new HttpEntity<>(payload.toString(), headers);
		        
		        // Send request using RestTemplate
		        RestTemplate restTemplate = new RestTemplate();
		        String url = EthPaymentApiUrl + "/get-wallet-balance";
		        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		        	
		        JSONObject jsonResponse = new JSONObject(response.getBody());
		        senderWalletBalance = jsonResponse.optString("balance", "");
		        
		        logger.info("Connection successful and received wallet balance: "+senderWalletBalance);

		    	model.addAttribute("balance", senderWalletBalance);
	        }
	        catch(Exception e) {
		    	model.addAttribute("error", "Process failed due to error: " + e.getStackTrace());
		    	e.printStackTrace();
	        }
	    } 
	    else 
	    	model.addAttribute("error", "Process failed due to connection issue with status code: " + connectionResponse.getStatusCode());
	           
    	model.addAttribute("walletTransactionDto1", new WalletTransactionDto1());
    	model.addAttribute("ethAmount", ethAmount);
    	//model.addAttribute("receiverAddress", receiver_metamask_walletAddress);
		return "ethPayment";
	}
	
	@PostMapping(value="/perform-transaction")
	public String performTransaction(@ModelAttribute WalletTransactionDto1 walletTransactionDto1, Model model) {
		ResponseEntity<String> connectionResponse = this.connectToInfura();
		
	    if (connectionResponse.getStatusCode() != HttpStatus.OK) { 
	    	model.addAttribute("walletTransactionDto1", new WalletTransactionDto1());
        	model.addAttribute("ethAmount", walletTransactionDto1.getEthAmount());
	    	model.addAttribute("error", "Process failed due to connection issue with status code: " + connectionResponse.getStatusCode());
	    	return "ethPayment";
	    }
	        
	    try {
	    	walletTransactionDto1.setSenderAddress(walletTransactionDto1.getSenderAddress().trim());
		    walletTransactionDto1.setSenderPrivateKey(walletTransactionDto1.getSenderPrivateKey().trim());
		        
		    // Build the request
		    JSONObject payload = new JSONObject();
		    payload.put("infura_project_id", infuraProjectId);
		    payload.put("sender_address", walletTransactionDto1.getSenderAddress());
		    payload.put("private_key", walletTransactionDto1.getSenderPrivateKey());
		    payload.put("recipient_address", receiver_metamask_walletAddress);
		    payload.put("eth_amount", walletTransactionDto1.getEthAmount());
		        
		    logger.info("eth_amount: {}", walletTransactionDto1.getEthAmount());
		        
		    HttpHeaders headers = new HttpHeaders();
		    headers.setContentType(MediaType.APPLICATION_JSON);
		        
		    HttpEntity<String> requestEntity = new HttpEntity<>(payload.toString(), headers);
		        
		    // Send request using RestTemplate
		    RestTemplate restTemplate = new RestTemplate();
		    String url = EthPaymentApiUrl + "/send_transaction";
		    ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, String.class);
		    JSONObject jsonResponse = new JSONObject(response.getBody());
		        
		    if(response.getStatusCode() == HttpStatus.BAD_REQUEST || response.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
		    	model.addAttribute("walletTransactionDto1", new WalletTransactionDto1());
		        model.addAttribute("ethAmount", walletTransactionDto1.getEthAmount());
		        model.addAttribute("error", "Process failed due to the error: "+jsonResponse.optString("message"));
		        return "ethPayment";
		    }  
		    String txHash = jsonResponse.getString("transaction_hash");
		    int blockNumber = jsonResponse.getInt("block_number");
		    double updatedBalance = jsonResponse.getDouble("updated_balance");
		        
		    logger.info("Payment with status: "+jsonResponse.getString("status"));
		    logger.info("EthAmount: {}",walletTransactionDto1.getEthAmount());
		    return orderComplete(txHash, String.valueOf(walletTransactionDto1.getEthAmount()), model);
	     }
	     catch(Exception e) {
	    	model.addAttribute("walletTransactionDto1", new WalletTransactionDto1());
	        model.addAttribute("ethAmount", walletTransactionDto1.getEthAmount());
		    model.addAttribute("error", "Process failed due to error: " + e.getStackTrace());
		    e.printStackTrace();
		    return "ethPayment";
	     } 
	}
	/*
	@PostMapping(value="/recordTransaction")
	public ResponseEntity<String> recordTransactionAndShow(@RequestBody TransactionReceiptDto txnReceipt) {
		logger.info("transaction hash: {}\n sender: {}\n recipient: {}\n amount: {}ETH\n",
				txnReceipt.getTransactionHash(), 
				txnReceipt.getSenderAddress(),
				txnReceipt.getRecipientAddress(),
				txnReceipt.getEthAmount()
			);
		
		return ResponseEntity.ok("transaction is successful by metamask");
	}*/
	
	public String orderComplete(String txnHash, String total, Model model) {
		model.addAttribute("cart", cartService.getAuthenticatedUserCart().getCartProducts());
		model.addAttribute("total", total);
		model.addAttribute("txnHash", txnHash);
		return "orderConfirm";
	}
}
