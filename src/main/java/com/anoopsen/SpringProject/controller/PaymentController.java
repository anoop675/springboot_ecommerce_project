package com.anoopsen.SpringProject.controller;

import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anoopsen.SpringProject.repository.CartRepo;
import com.anoopsen.SpringProject.service.CartService;
import com.anoopsen.SpringProject.service.CryptoService;
import com.anoopsen.SpringProject.service.PaymentService;
import com.anoopsen.SpringProject.model.Cart;
import com.anoopsen.SpringProject.model.User;

@Controller
@RequestMapping(value="/VITproject")
public class PaymentController {
	/*
	@Value("${ethpaymentapi.url}")
	private String EthPaymentApiUrl;
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
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
	/*@GetMapping(value="/payNow/eth/{ethAmount}")
	public void paymentInEther(@ ) {
		System.out.println("jafadjbjsjfjgbdgf");
	}*/
}
