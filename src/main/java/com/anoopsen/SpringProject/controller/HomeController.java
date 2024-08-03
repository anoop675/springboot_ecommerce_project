package com.anoopsen.SpringProject.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.anoopsen.SpringProject.service.CategoryService;
import com.anoopsen.SpringProject.service.ProductService;

@Controller
@RequestMapping(value="/VITproject")
public class HomeController {
	
	@Autowired
	CategoryService category_service;
	
	@Autowired
	ProductService product_service;
	
	@GetMapping(value={"/", "/home"})
	public String home(Model model) {
		return "index";
	}
	
	@GetMapping("/shop")
	public String shop(Model model) {
		model.addAttribute("categories", category_service.getAllCategory());
		model.addAttribute("products", product_service.getAllProduct());
		
		return "shop";
	}
		
}
