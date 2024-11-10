package com.anoopsen.SpringProject.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.stereotype.Service;

import com.anoopsen.SpringProject.model.Cart;
import com.anoopsen.SpringProject.model.CartProduct;
import com.anoopsen.SpringProject.model.Product;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.repository.CartProductRepository;
import com.anoopsen.SpringProject.repository.CartRepo;
import com.anoopsen.SpringProject.repository.UserRepository;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;

/* Code when Cart directly had list of Products
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

	public boolean isCartEmpty() {
		return this.getAuthenticatedUserCart().getProducts().isEmpty();
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
}*/
@Service
public class CartService {
    
    Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    CartRepo cartRepo;

    @Autowired
    UserRepository userRepo;

    @Autowired
    CartProductRepository cartProductRepo; // Add a repository for CartProduct
    
    public void createEmptyCart(User user) {
        Cart emptyCart = new Cart();
        emptyCart.setTotal(0);
        emptyCart.setUser(user);
        emptyCart.setCartProducts(new ArrayList<>());
        cartRepo.save(emptyCart);
    }

    private User getAuthenticatedUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication instanceof OAuth2AuthenticationToken) {
            OAuth2AuthenticationToken oauthToken = (OAuth2AuthenticationToken) authentication;
            String currentUserEmail = oauthToken.getPrincipal().getAttribute("email");
            logger.info("OAuth2 authenticated user email: " + currentUserEmail);
            
            return userRepo.findUserByEmail(currentUserEmail)
                .orElseThrow(() -> new RuntimeException("User not found"));    
        } 
        else if (authentication instanceof UsernamePasswordAuthenticationToken || authentication instanceof RememberMeAuthenticationToken) {
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

    public boolean isCartEmpty() {
        return this.getAuthenticatedUserCart().getCartProducts().isEmpty();
    }

    public int getCartCount() {
        Cart userCart = this.getAuthenticatedUserCart();
        return userCart.getCartProducts()
            .stream()
            .mapToInt(CartProduct::getQuantity)
            .sum();
    }

    public double getCartTotal() {
        Cart userCart = this.getAuthenticatedUserCart();
        double total = userCart.getCartProducts()
            .stream()
            .mapToDouble(cartProduct -> cartProduct.getProduct().getPrice() * cartProduct.getQuantity())
            .sum();
        userCart.setTotal(total);
        
        return total;
    }

    public ResponseEntity<String> addProductToCart(Product product) {
        Cart cart = this.getAuthenticatedUserCart();
        
        // Check if the product is already in the cart
        CartProduct cartProduct = cart.getCartProducts()
            .stream()
            .filter(cp -> cp.getProduct().getId() == product.getId())
            .findFirst()
            .orElse(null);

        if (cartProduct != null) {
            cartProduct.setQuantity(cartProduct.getQuantity() + 1); // If the product is already in the cart, update the quantity
        } 
        else {
            cartProduct = new CartProduct(); // Otherwise, create a new CartProduct entity
            cartProduct.setProduct(product);
            cartProduct.setCart(cart);
            cartProduct.setQuantity(1);
            cart.getCartProducts().add(cartProduct);
        }

        cartProductRepo.save(cartProduct);
        cartRepo.save(cart);
        /*
         Run SQL query to check:
            SELECT * FROM USERS 
			JOIN CART_TABLE 
			ON USERS.user_id = CART_TABLE.user_id
			JOIN CART_PRODUCT
			ON CART_TABLE.cart_id = CART_PRODUCT.cart_id;
         */

        return ResponseEntity.status(HttpStatus.OK).body("Product added to cart successfully");
    }
    /*
    public void removeProductFromCart(int productId) {
        Cart cart = this.getAuthenticatedUserCart();
        List<CartProduct> cartProducts = cart.getCartProducts();
        
        logger.info("product_id attempted to be deleted: "+productId);
        logger.info("product: "+cartProducts.stream().map(cp -> cp.getProduct().getId()).findFirst().get());
        
        // Find the CartProduct by productId and remove it
        CartProduct cartProductToRemove = cartProducts.stream()
        	.filter(cp -> cp.getProduct().getId() == productId)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Product not found in cart"));
        
        cartProducts.remove(cartProductToRemove);
        cartRepo.save(cart);
        cartProductRepo.delete(cartProductToRemove); // Delete the CartProduct entity
    }*/

    @Transactional
    public void removeProductFromCart(int productId, int quantityToRemove) {
        Cart cart = this.getAuthenticatedUserCart();
        List<CartProduct> cartProducts = cart.getCartProducts();

        logger.info("product_id attempted to be reduced: " + productId);

        CartProduct cartProductToUpdate = cartProducts.stream()
            .filter(cp -> cp.getProduct().getId() == productId)
            .findFirst()
            .orElseThrow(() -> new RuntimeException("Product not found in cart"));

        int newQuantity = cartProductToUpdate.getQuantity() - quantityToRemove;
        
        if (newQuantity <= 0) {
            logger.info("CartProduct {} quantity updated to {} in cart {}.", cartProductToUpdate.getId(), newQuantity, cart.getId());
            
            //cartProducts.remove(cartProductToUpdate.getId());
            cartProductRepo.deleteCartProductById(cartProductToUpdate.getId());
            cartRepo.save(cart);
      
            logger.info("CartProduct removed from cart's list.");
        } 
        else {
            cartProductToUpdate.setQuantity(newQuantity); // Update quantity
            cartProductRepo.save(cartProductToUpdate); // Save updated quantity
            logger.info("CartProduct quantity updated.");
        }
    }

    @Transactional
    public void deleteCartById(int cartId) {
        // Delete all cartProduct entries related to the cart
        cartProductRepo.deleteByCartId(cartId);

        // Now delete the cart itself
        cartRepo.deleteById(cartId);
    }
}

