package com.anoopsen.SpringProject.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anoopsen.SpringProject.model.Cart;
import com.anoopsen.SpringProject.model.Product;
import com.anoopsen.SpringProject.service.CartService;
import com.anoopsen.SpringProject.service.ProductService;

@Controller
@RequestMapping(value="/VITproject")
public class CartController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CartService cartService;
	
	@GetMapping(value="/cart")
	public String cartGet(Model model) {
		//TODO: Fetch cart from DB after dynamically updating total and products----
		Cart cart = cartService.getAuthenticatedUserCart();
		model.addAttribute("cartCount", cart.getProducts().size());
		Double total = cartService.getCartTotal();
		model.addAttribute("total", total);
		model.addAttribute("cart", cart.getProducts());
		return "cart";
	}
	
	@GetMapping(value="/addToCart/{id}")
	public String addToCart(@PathVariable int id) {
		cartService.addProductToCart(productService.get_productById(id));
		return "redirect:/VITproject/shop";
	}
	
	@GetMapping(value="/cart/removeItem/{id}")
	public String cartItemRemove(@PathVariable int id) {
		cartService.removeProductFromCart(id);
		return "redirect:/VITproject/cart";
	}
	
	/*@GetMapping(value="/checkout")
	public String checkout(Model model, RedirectAttributes redirectAttributes) {
	    if(Cart.cart.isEmpty()) {
	    	logger.info("Cart is Empty?: "+Cart.cart.isEmpty());
	        redirectAttributes.addFlashAttribute("error_message", "Sorry, your cart is empty");
	        return "redirect:/VITproject/cart";	
	    }
	    
	    Double total = Cart.cart.stream().mapToDouble(product -> product.getPrice()).sum();
		model.addAttribute("total", total);
		return "checkout";
	}*/
}
