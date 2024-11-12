package com.anoopsen.SpringProject.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.anoopsen.SpringProject.repository.CartRepo;
import com.anoopsen.SpringProject.service.CartService;
import com.anoopsen.SpringProject.service.CryptoService;
import com.anoopsen.SpringProject.service.PaymentService;
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
	
	@Autowired
	CartRepo cartRepo;

	@Autowired
	CartService cartService;
	
	@Autowired
	CryptoService cryptoService;
	
	@Autowired
	PaymentService paymentService;
	
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
}
