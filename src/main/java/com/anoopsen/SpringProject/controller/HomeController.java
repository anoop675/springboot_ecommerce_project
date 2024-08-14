package com.anoopsen.SpringProject.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anoopsen.SpringProject.model.Category;
import com.anoopsen.SpringProject.model.Product;
import com.anoopsen.SpringProject.service.CategoryService;
import com.anoopsen.SpringProject.service.ProductService;

@Controller
@RequestMapping(value="/VITproject")
public class HomeController {
	
	@Autowired
	CategoryService category_service;
	
	@Autowired
	ProductService product_service;
	
	//@GetMapping(value={"/", "/home"})
	@GetMapping(value="/home")
	public String home(Model model) {
		return "index";
	}
	
	@GetMapping(value="/shop")
	public String shop(Model model) {
		model.addAttribute("categories", category_service.getAllCategory());
		model.addAttribute("products", product_service.getAllProduct());
		
		return "shop";
	}
	
	@GetMapping(value="/shop/category/{id}")
	public String shopByCategory(@PathVariable int id, Model model) {
		List<Category> categories = category_service.getAllCategory();
		List<Product> productsByCategory = product_service.getAllProductByCategoryId(id);
		
		model.addAttribute("categories", categories);
		model.addAttribute("products", productsByCategory);
		
		return "shop";
	}
}
