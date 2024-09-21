package com.anoopsen.SpringProject.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.anoopsen.SpringProject.model.Cart;
import com.anoopsen.SpringProject.model.Product;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.repository.CartRepo;
import com.anoopsen.SpringProject.repository.UserRepository;

@Service
public class CartService {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	CartRepo cartRepo;
	
	@Autowired
    UserRepository userRepo;
	
	private User getAuthenticatedUser() {
	    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

	    if (authentication instanceof OAuth2AuthenticationToken) {
	        OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
	        String currentUserEmail = oauthToken.getPrincipal().getAttribute("email");
	        logger.info("OAuth2 authenticated user email: " + currentUserEmail);
	        
	        return userRepo.findUserByEmail(currentUserEmail)
	            .orElseThrow(() -> new RuntimeException("User not found"));    
	    } 
	    else if (authentication instanceof UsernamePasswordAuthenticationToken) {
	        User user = (User) authentication.getPrincipal();
	        String currentUserEmail = user.getEmail();
	        logger.info("UsernamePassword authenticated user email: " + currentUserEmail);
	        
	        return userRepo.findUserByEmail(currentUserEmail)
	            .orElseThrow(() -> new RuntimeException("User not found"));
	    }

	    throw new RuntimeException("Authentication type not supported");
	}

	
	public Cart getAuthenticatedUserCart() {
	    User currentUser = getAuthenticatedUser();
	    return cartRepo.findByUserEmail(currentUser.getEmail())
	        .orElseGet(() -> {
	            Cart newCart = new Cart();
	            newCart.setUser(currentUser);
	            return cartRepo.save(newCart);
	        });
	}

	
	public void createEmptyCart(User user) {
		Cart cart = new Cart();
		cart.setTotal(0);
		cart.setProducts(new ArrayList<>());
		cart.setUser(user);
		cartRepo.save(cart);
	}
	
	public int getCartCount() {
        Cart userCart = this.getAuthenticatedUserCart();
        if (userCart != null && userCart.getProducts() != null) {
        	return userCart.getProducts().size();
        }
        return 0; // Return 0 if the cart is null or empty
    }
	
	public double getCartTotal() {
		Cart userCart = this.getAuthenticatedUserCart();
        userCart.setTotal(userCart.getProducts()
        		.stream()
        		.mapToDouble(product -> product.getPrice())
        		.sum());
        return userCart.getTotal();
	}
	
	public ResponseEntity<String> addProductToCart(Product product) {
		Cart cart = this.getAuthenticatedUserCart();
		List<Product> products = cart.getProducts();
		products.add(product);
		cart.setProducts(products);
		cart.setTotal(cart.getTotal()+product.getPrice());
		cartRepo.save(cart);
		return ResponseEntity.status(HttpStatus.OK).body("Product added to cart successfully");
	}
	
	public void removeProductFromCart(int index) {
		Cart cart = this.getAuthenticatedUserCart();
		List<Product> products = cart.getProducts();
		products.remove(index);
		cartRepo.save(cart);
	}
}
