package com.anoopsen.SpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anoopsen.SpringProject.global.Cart;
import com.anoopsen.SpringProject.model.Product;
import com.anoopsen.SpringProject.service.ProductService;

@Controller
@RequestMapping(value="/VITproject")
public class CartController {
	
	@Autowired
	ProductService productService;
	
	@GetMapping(value="/cart")
	public String cartGet(Model model) {
		model.addAttribute("cartCount", Cart.cart.size());
		Double total = Cart.cart.stream().mapToDouble(product -> product.getPrice()).sum();
		model.addAttribute("cartCount", Cart.cart.size());
		model.addAttribute("total", total);
		model.addAttribute("cart", Cart.cart);
		return "cart";
	}
	
	@GetMapping(value="/addToCart/{id}")
	public String addToCart(@PathVariable int id) {
		Cart.cart.add(productService.get_productById(id));
		return "redirect:/VITproject/shop";
	}
	
	@GetMapping(value="/cart/removeItem/{index}")
	public String cartItemRemove(@PathVariable int index) {
		Cart.cart.remove(index);
		return "redirect:/VITproject/cart";
	}
	
	@GetMapping(value="/checkout")
	public String checkout(Model model) {
		Double total = Cart.cart.stream().mapToDouble(product -> product.getPrice()).sum();
		model.addAttribute("total", total);
		return "checkout";
	}
}
