package com.anoopsen.SpringProject.controller;

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
	
	@PostMapping(value="/connect")
    public String connect(RedirectAttributes attr) {
		JSONObject payload = new JSONObject();
		HttpHeaders headers = new HttpHeaders();
        try {
            
            payload.put("infura_project_id", infuraProjectId);

            
            headers.set("Content-Type", "application/json");

            HttpEntity<String> requestEntity = new HttpEntity<>(payload.toString(), headers);

            RestTemplate restTemplate = new RestTemplate();
            String url = EthPaymentApiUrl + "/connect";

            ResponseEntity<String> response = restTemplate.exchange(
                url,
                HttpMethod.POST,
                requestEntity,
                String.class
            );

            String jsonResponse = response.getBody();
            JSONObject jsonObject = new JSONObject(jsonResponse);
            attr.addAttribute("response", jsonObject);
            attr.addAttribute("showModal", true); // Indicating to show modal
            
            logger.info("Connection status: {} with body:\n{}", response.getStatusCode(), jsonResponse);


        } catch (Exception e) {
            attr.addAttribute("showModal", false); // Indicating to show modal
            attr.addAttribute("error", "Error while connecting: " + e.getMessage());
            
            e.printStackTrace();
        }
        return "redirect:/VITproject/checkout";
    }
	
	@PostMapping(value="/create-wallet")
	public ResponseEntity<String> createWallet() {
		
		
		return ResponseEntity.ok("Wallet created successfully!");
	}
	
	@PostMapping(value="/perform-transaction")
	public ResponseEntity<String> performTransaction(@ModelAttribute WalletTransactionDto1 walletTransactionDto1) {
		
		
		return ResponseEntity.ok("Transaction is done successfully!");
	}
	
	@PostMapping(value="/recordTransaction")
	public ResponseEntity<String> recordTransactionAndShow(@RequestBody TransactionReceiptDto txnReceipt) {
		logger.info("transaction hash: {}\n sender: {}\n recipient: {}\n amount: {}ETH\n",
				txnReceipt.getTransactionHash(), 
				txnReceipt.getSenderAddress(),
				txnReceipt.getRecipientAddress(),
				txnReceipt.getEthAmount()
			);
		
		return ResponseEntity.ok("transaction is successful by metamask");
	}
	
	@GetMapping(value="/orderConfirm")
	public String orderComplete(@RequestParam("transactionHash") String txnHash, @RequestParam("total") String total, Model model) {
		model.addAttribute("cart", cartService.getAuthenticatedUserCart().getCartProducts());
		model.addAttribute("total", total);
		model.addAttribute("txnHash", txnHash);
		return "orderConfirm";
	}
}
