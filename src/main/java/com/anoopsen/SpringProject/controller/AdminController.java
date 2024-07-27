package com.anoopsen.SpringProject.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.anoopsen.SpringProject.dto.ProductDto;
import com.anoopsen.SpringProject.model.Category;
import com.anoopsen.SpringProject.model.Product;
import com.anoopsen.SpringProject.service.CategoryService;
import com.anoopsen.SpringProject.service.ProductService;

@Controller
@RequestMapping(value="/VITproject")
public class AdminController {
	
	@Autowired
	CategoryService category_service;
	
	@Autowired
	ProductService product_service;
	
	@GetMapping(value="/admin")
	public String adminHome() {
		return "adminHome";
	}
	
	//Category Section
	
	@GetMapping(value="/admin/categories")
	public String getCategories(Model model) {
		List<Category> categories = category_service.getAllCategory(); //sending all category objects to the form
		model.addAttribute("categories", categories);
		return "categories";
	}
	
	@GetMapping(value="/admin/categories/add")
    public String getCategoriesAdd(Model model) {
		Category category = new Category(); //this is an empty Category object
        model.addAttribute("category", category); //sending empty Category object
        /*
         	The form itself, will bind the data for each attribute in the empty Category object 
         */
        model.addAttribute("message", "Enter new category to be added");
        return "categoriesAdd";
    }
	
	@PostMapping(value="/admin/categories/add")                                    
	public String postCategoriesAdd(@ModelAttribute("category") Category category, RedirectAttributes redirectAttributes) { //you should use RedirectAttributes when you need to pass data to another controller after performing a redirect.
	    category_service.addCategory(category);
	    redirectAttributes.addFlashAttribute("response_message", "Category added/updated successfully!"); //this will give a message as feedback to form
	    return "redirect:/VITproject/admin/categories";
	}

	//NOTE: By default, clicking an <a> tag sends a GET request to the server, retrieving information from the linked URL.
	@GetMapping(value="/admin/categories/delete/{id}")
	public String deleteCategory(@PathVariable int id, RedirectAttributes redirectAttributes) { //extract 'id' value from the URI path
		category_service.removeCategory(id);
	    redirectAttributes.addFlashAttribute("response_message", "Category deleted successfully!"); //this will give a message as feedback to form

		return "redirect:/VITproject/admin/categories";
	}
	
	@GetMapping(value="/admin/categories/update/{id}")
	public String updateCategory(@PathVariable int id, Model model) { //extract 'id' value from the URI path, 
		Optional<Category> category = category_service.getCategoryById(id);
		if(category.isPresent()) {
			model.addAttribute("message", "Enter the updated category");
			model.addAttribute("category", category.get());

			return "categoriesAdd";
		}
		else { 
			return "404";
		}
	}
	
	//Product Section
	
	@GetMapping(value="/admin/products")
	public String getProducts(Model model) {
		List<Product> products = product_service.getAllProduct();
		model.addAttribute("products", products);
		
		return "products";
	}
	
	@GetMapping(value="/admin/products/add")
	public String getProductAdd(Model model) {
		ProductDto productDto = new ProductDto();
		List<Category> categories = category_service.getAllCategory();
		model.addAttribute("productDTO", productDto);
		model.addAttribute("categories", categories);
		
		return "productAdd";
		
	}
	
	@PostMapping(value="/admin/products/add")
	public String getProductAdd(@ModelAttribute("productDto") ProductDto productDto) {
		//product_service.
		
		return "productAdd";
		
	}
}
