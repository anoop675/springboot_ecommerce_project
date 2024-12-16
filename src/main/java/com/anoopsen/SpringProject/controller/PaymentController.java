package com.anoopsen.SpringProject.controller;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.TreeMap;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
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
import com.anoopsen.SpringProject.model.Cart;
import com.anoopsen.SpringProject.model.User;

@Controller
@RequestMapping(value="/VITproject")
public class PaymentController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	/*
	@Value("${ethpaymentapi.url}")
	private String EthPaymentApiUrl;
	*/
	@Autowired
	CartRepo cartRepo;

	@Autowired
	CartService cartService;
	
	@Autowired
	CryptoService cryptoService;
	
	@Autowired
	PaymentService paymentService;
	/*
	@GetMapping(value="/payNow/eth")
	public void paymentInEther() {
		User thisUser = cartService.getAuthenticatedUserCart().getUser();
		Optional<Cart> thisUserCart = cartRepo.findById(thisUser.getCart().getId());
		
		if(thisUserCart.isPresent()) {
			double currentEthValue = cryptoService.getEthToInrRate();
			double orderTotal = thisUserCart.get().getTotal();
			double amountInEth = orderTotal / currentEthValue;
			
			logger.info("TODO: get wallet addresses from API and store in database, then call API (EthPaymentApiUrl)");
			
			paymentService.sendEthValue(EthPaymentApiUrl, amountInEth);
		}
		else {
			System.out.println("Cart not found for user with name " + thisUser.getFirstName());
		}
	}*/
	/*
	@PostMapping("/recordTransaction")
    public String recordTransaction(@RequestBody TransactionReceiptDto transactionDetails, Model model) {
        // Process transaction details (e.g., save to database)
        logger.info("Received transaction: " + transactionDetails);
        model.addAttribute();
        // Simulate saving transaction and returning response
        return 
    }*/
	/*
	@Autowired
    private PaytmDetails paytmDetails;

    @Value("${paytm.mobile}")
    private String paytmMobile;

    @Value("${paytm.email}")
    private String paytmEmail;
    
    @PostMapping(value = "/paytm/make-payment")
    public ModelAndView getPaymentRedirect(@RequestParam String orderId, 
                                           @RequestParam String txnAmount, 
                                           @RequestParam String customerId) throws Exception {
        logger.info("Initiating payment for orderId: {}, amount: {}, customerId: {}", orderId, txnAmount, customerId);

        // Set the Paytm payment URL for redirect
        ModelAndView modelAndView = new ModelAndView("redirect:" + paytmDetails.getPaytmUrl());
        TreeMap<String, String> parameters = new TreeMap<>();

        // Populate Paytm parameters from configured details
        paytmDetails.getDetails().forEach(parameters::put);
        parameters.put("MOBILE_NO", paytmMobile);
        parameters.put("EMAIL", paytmEmail);
        parameters.put("ORDER_ID", orderId);
        parameters.put("TXN_AMOUNT", txnAmount);
        parameters.put("CUST_ID", customerId);

        // Generate the checksum to secure the transaction
        String checkSum = getCheckSum(parameters);
        parameters.put("CHECKSUMHASH", checkSum);

        modelAndView.addAllObjects(parameters);
        return modelAndView;
    }
    
    @PostMapping(value = "/paytm/payment-response")
    public ModelAndView getPaymentResponseRedirect(HttpServletRequest request) {
        ModelAndView modelAndView = new ModelAndView("redirect:http://localhost:8080/#/payment"); // Redirect to payment result page
        Map<String, String[]> mapData = request.getParameterMap();
        TreeMap<String, String> parameters = new TreeMap<>();
        String paytmChecksum = "";

        // Extract parameters from the response
        for (Entry<String, String[]> entry : mapData.entrySet()) {
            if ("CHECKSUMHASH".equalsIgnoreCase(entry.getKey())) {
                paytmChecksum = entry.getValue()[0];
            } else {
                parameters.put(entry.getKey(), entry.getValue()[0]);
            }
        }

        // Verify the checksum and determine the payment result
        String result;
        try {
            boolean isValidChecksum = validateCheckSum(parameters, paytmChecksum);
            if (isValidChecksum && "01".equals(parameters.get("RESPCODE"))) {
                result = "Payment Successful";
            } else if (isValidChecksum) {
                result = "Payment Failed";
            } else {
                result = "Checksum Mismatched";
            }
        } catch (Exception e) {
            result = "Error: " + e.getMessage();
        }

        modelAndView.addObject("result", result);
        parameters.remove("CHECKSUMHASH"); // Remove checksum from parameters for display
        modelAndView.addObject("parameters", parameters);
        return modelAndView;
    }

    private boolean validateCheckSum(TreeMap<String, String> parameters, String paytmChecksum) throws Exception {
        return PaytmChecksum.verifySignature(parameters, paytmDetails.getMerchantKey(), paytmChecksum);
    }
    
    private String getCheckSum(TreeMap<String, String> parameters) throws Exception {
        return PaytmChecksum.generateSignature(parameters, paytmDetails.getMerchantKey());
    }*/
	@PostMapping(value="/recordTransaction")
	public ResponseEntity<String> recordTransactionAndShow(@RequestBody TransactionReceiptDto txnReceipt) {
		//TODO: Save the order details in the database
		logger.info("transaction hash: {}\n sender: {}\n recipient: {}\n amount: {}ETH\n",
				txnReceipt.getTransactionHash(), 
				txnReceipt.getSenderAddress(), 
				txnReceipt.getRecipientAddress(), 
				txnReceipt.getEthAmount()
				);
		
		return ResponseEntity.ok("transaction is successful by metamask");
	}
	
	@GetMapping(value="/orderConfirm")
	public String orderComplete(Model model) {
		model.addAttribute("cart", cartService.getAuthenticatedUserCart().getCartProducts());
		return "orderConfirm";
	}
}
