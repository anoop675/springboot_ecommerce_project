package com.anoopsen.SpringProject.service;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.anoopsen.SpringProject.model.Cart;
import com.anoopsen.SpringProject.model.Product;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.repository.CartRepo;
import com.anoopsen.SpringProject.repository.UserRepository;

@Service
public class CartService {
	
	@Autowired
	CartRepo cartRepo;
	
	@Autowired
    UserRepository userRepo;
	
	public Cart getCart() {
		 Authentication authentication = SecurityContextHolder.getContext().getAuthentication(); // Gets the current authenticated user at runtime
	     String currentUserEmail = authentication.getName();  // email is used as the username
	     User currentUser = userRepo.findUserByEmail(currentUserEmail)
	        						.orElseThrow(() -> new RuntimeException("User not found")); // Fetch the user from the UserRepo based on the authenticated user's email
	        
	     Cart userCart = cartRepo.findByUser(currentUser.getEmail())
	    		 				 .orElseThrow(() -> new RuntimeException("Cart not found"));;  // Gets the current authenticated user
	     
	     return userCart;
	}
	
	public int getCartCount() {
        Cart userCart = this.getCart();
        if (userCart != null && userCart.getProducts() != null) {
        	return userCart.getProducts().size();
        }
        return 0; // Return 0 if the cart is null or empty
    }
	
	public double getCartTotal() {
		Cart userCart = this.getCart();
        userCart.setTotal(userCart.getProducts()
        		.stream()
        		.mapToDouble(product -> product.getPrice())
        		.sum());
        return userCart.getTotal();
	}
	
	public void addProductToCart(Product product) {
		Cart userCart = this.getCart();
		userCart.
	}
}
