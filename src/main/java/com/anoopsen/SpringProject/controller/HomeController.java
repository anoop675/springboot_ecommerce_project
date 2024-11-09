/*
 * TODO: - Display Recently viewed products
 * 		 - Product Recommendation System using model
 */
package com.anoopsen.SpringProject.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anoopsen.SpringProject.model.Category;
import com.anoopsen.SpringProject.model.Product;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.service.CartService;
import com.anoopsen.SpringProject.service.CategoryService;
import com.anoopsen.SpringProject.service.ProductService;
import com.anoopsen.SpringProject.service.RatingService;
import com.anoopsen.SpringProject.service.UserService;

@Controller
@RequestMapping(value="/VITproject")
public class HomeController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	CategoryService category_service;
	
	@Autowired
	ProductService product_service;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	UserService userService;
	
	@Autowired
	RatingService ratingService;
	
	@GetMapping(value="/shop")
	public String home(Model model) {
		
		String firstName = userService.getAuthenticatedUserFirstName();
		
		model.addAttribute("firstname", firstName);
	    
	    logger.info(SecurityContextHolder.getContext().getAuthentication().toString());
	    
	    model.addAttribute("cartCount", cartService.getCartCount());
	    model.addAttribute("categories", category_service.getAllCategory());
		model.addAttribute("products", product_service.getAllProduct());
		//model.addAttribute("cartCount", Cart.cart.size());
	    
	    return "shop";
	}
	
	@GetMapping(value="/shop/category/{id}")
	public String shopByCategory(@PathVariable int id, Model model) {
		
		String firstName = userService.getAuthenticatedUserFirstName();
		
		model.addAttribute("firstname", firstName);
	    model.addAttribute("cartCount", cartService.getCartCount());
	   
		List<Category> categories = category_service.getAllCategory();
		List<Product> productsByCategory = product_service.getAllProductByCategoryId(id);
		
		model.addAttribute("categories", categories);
		model.addAttribute("products", productsByCategory);
		
		return "shop";
	}
	
	@GetMapping(value="/shop/viewproduct/{id}")
	public String viewProduct(@PathVariable int id, Model model) {
		Product product = product_service.get_productById(id);
		
		model.addAttribute("product", product);
		model.addAttribute("cartCount", cartService.getCartCount());
		model.addAttribute("productRating", ratingService.getAverageRating(id));
		
		return "viewProduct";
	}
	
	@GetMapping(value="/rate/{id}/{rating}")
	public String rateProductPost(@PathVariable int id, @PathVariable Integer rating) throws Exception {
		
		logger.info("rateProductPost called");
	    // Ensure that rating is not null
	    if (rating != null) {
	        ratingService.rateProduct(id, rating);
	    }
	    return "redirect:/VITproject/shop/viewproduct/{id}";
	}
}
