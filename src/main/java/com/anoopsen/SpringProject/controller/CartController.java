package com.anoopsen.SpringProject.controller;

import java.text.DecimalFormat;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
import com.anoopsen.SpringProject.model.CartProduct;
import com.anoopsen.SpringProject.model.Product;
import com.anoopsen.SpringProject.service.CartService;
import com.anoopsen.SpringProject.service.CryptoService;
import com.anoopsen.SpringProject.service.ProductService;

@Controller
@RequestMapping(value="/VITproject")
public class CartController {
	
	Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	ProductService productService;
	
	@Autowired
	CartService cartService;
	
	@Autowired
	CryptoService cryptoService;
	
	@GetMapping(value="/cart")
	public String cartGet(Model model) {
		Cart cart = cartService.getAuthenticatedUserCart();
		double total = cartService.getCartTotal();
		/* Since CartTable is defined
		List<Product> cart.getProducts();
		*/
		System.out.println(total);
		List<CartProduct> cartProducts = cart.getCartProducts();
	    
	    // Cart count by summing the quantities
	    int cartCount = cartProducts.stream()
	                                .mapToInt(CartProduct::getQuantity)
	                                .sum();
	    
	    // get the list of products
	    List<Product> products = cartProducts.stream()
	                                         .map(CartProduct::getProduct)
	                                         .toList();
		
		model.addAttribute("cartCount", cartCount);
		model.addAttribute("total", total);
		model.addAttribute("cart", cartProducts);
		//model.addAttribute("productQuantityMap", productQuantityMap);
		return "cart";
	}
	
	@GetMapping(value="/addToCart/{id}")
	public String addToCart(@PathVariable int id) {
		cartService.addProductToCart(productService.get_productById(id));
		return "redirect:/VITproject/shop";
	}
	
	@GetMapping(value = "/cart/removeItem/{id}/{quantity}")
	public String cartItemRemove(@PathVariable int id, @PathVariable int quantity) {
		//System.out.println(cartService.getAuthenticatedUserCart().getCartProducts().stream().filter(cp -> cp.getProduct().getId() == id).findFirst().get());
	    cartService.removeProductFromCart(id, quantity);
	    return "redirect:/VITproject/cart";
	}
	
	@GetMapping(value="/checkout")
	public String checkout(Model model, RedirectAttributes redirectAttributes) {
		DecimalFormat decimalFormat = new DecimalFormat("#.######"); // Set to 6 decimal places or adjust as needed
		boolean cartEmpty = cartService.isCartEmpty();
		double total;
		double ethToInrRate;
		if(cartEmpty) {
	    	logger.info("Cart is Empty?: "+cartEmpty);
	        redirectAttributes.addFlashAttribute("error_message", "Sorry, your cart is empty");
	        return "redirect:/VITproject/cart";	
	    }
		total = cartService.getCartTotal();
		ethToInrRate = cryptoService.getEthToInrRate(); //fetches real-time Ether(ETH) to INR 
		String formattedEthAmount = decimalFormat.format(total / ethToInrRate);
		model.addAttribute("cartCount", cartService.getCartCount());
		model.addAttribute("total", total);
		model.addAttribute("eth_inr_rate", ethToInrRate);
		model.addAttribute("ethAmount", formattedEthAmount);
		return "checkout";
	}
}
