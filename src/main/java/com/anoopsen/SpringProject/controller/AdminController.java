package com.anoopsen.SpringProject.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
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
	
	private static final Logger logger = LoggerFactory.getLogger(AdminController.class);
	
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
        model.addAttribute("message", "Add new category");
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
			model.addAttribute("message", "Update category");
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
		
		model.addAttribute("message", "Add new product");
		model.addAttribute("productDto", productDto);
		model.addAttribute("categories", categories);
		
		return "productAdd";
		
	}
	
	@PostMapping(value="/admin/products/add")
	public String postProductAdd(@ModelAttribute("productDto") ProductDto productDto
								,@RequestParam("productImage") MultipartFile file
								,@RequestParam(value="imgName", required=false) String imgName
								,RedirectAttributes redirectAttributes) throws IOException {
		
		imgName = (imgName.equals(null)) ? productDto.getImageName() : imgName;
		
		
		logger.info(productDto.toString());
		logger.info(file.getOriginalFilename().toString());
		logger.info(imgName);
		
		
		product_service.addProduct(productDto, file, imgName);
		
		redirectAttributes.addAttribute("response_message", "Product added/updated successfully!");
		
		return "redirect:/VITproject/admin/products";
	}
	
	@GetMapping(value="/admin/products/delete/{id}")
	public String deleteProduct(@PathVariable int id, RedirectAttributes redirectAttributes) { 
		product_service.removeProductById(id);
	    redirectAttributes.addFlashAttribute("response_message", "Product deleted successfully!"); 

		return "redirect:/VITproject/admin/products";
	}
	
	@GetMapping(value="/admin/products/update/{id}")
	public String updateProduct(@PathVariable("id") int id, Model model) {
		ProductDto productDto = product_service.getProductById(id);
		List<Category> categories = category_service.getAllCategory();
		
		model.addAttribute("message", "Update product");
		model.addAttribute("productDto", productDto);
		model.addAttribute("categories", categories);
		
		return "productAdd";
	}
}
