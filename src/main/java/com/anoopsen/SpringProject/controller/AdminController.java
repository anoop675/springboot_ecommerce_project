package com.anoopsen.SpringProject.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.anoopsen.SpringProject.model.Role;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.service.CategoryService;
import com.anoopsen.SpringProject.service.ProductService;
import com.anoopsen.SpringProject.service.UserService;

@Controller
@RequestMapping(value="/VITproject")
public class AdminController {
	
	@Autowired
	CategoryService category_service;
	
	@Autowired
	ProductService product_service;
	
	@Autowired
	UserService userService;
	
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
		ResponseEntity<String> status = category_service.addCategory(category);
	    redirectAttributes.addFlashAttribute("response_message", status.getBody()); //this will give a message as feedback to form
	    return "redirect:/VITproject/admin/categories";
	}

	//NOTE: By default, clicking an <a> tag sends a GET request to the server, retrieving information from the linked URL.
	@GetMapping(value="/admin/categories/delete/{id}")
	public String deleteCategory(@PathVariable int id, RedirectAttributes redirectAttributes) { //extract 'id' value from the URI path
		//We need to check if products exist under that category. NOTE: Did not implemented DELETE ON CASCADE in Product table referencing Category table
		ResponseEntity<String> status = category_service.removeCategory(id, product_service);
		if(status.getStatusCode() == HttpStatus.INTERNAL_SERVER_ERROR) {
			redirectAttributes.addFlashAttribute("error_message", status.getBody());
		}
		else if(status.getStatusCode() == HttpStatus.OK) {
			redirectAttributes.addFlashAttribute("response_message", status.getBody());
		}
		else {
			redirectAttributes.addFlashAttribute("error_message", "Something went wrong, Please try again.");
		}
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
		else
			return "404";
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
		
		ResponseEntity<String> status = product_service.addProduct(productDto, file, imgName);
		redirectAttributes.addAttribute("response_message", status.getBody());
		
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
	
	@GetMapping(value="/admin/users")
	public String manageUsers(Model model) {
		List<User> users = userService.getAllUsers();
		model.addAttribute("users", users);
		return "users";
	}
	
	@GetMapping(value="/admin/users/delete/{id}")
	public String manageUsers(@PathVariable int id, RedirectAttributes redirectAttributes) {
		userService.removeUser(id);
		redirectAttributes.addFlashAttribute("response_message", "User deleted successfully!");
		return "redirect:/VITproject/admin/users";
	}
	
	@GetMapping(value="/admin/users/add")
	public String getUserAdd(){
		return "userAdd";
	}
	
	@PostMapping(value="/admin/users/add")
	public String postuserAdd(@ModelAttribute User user, RedirectAttributes redirectAttributes) throws Exception{
		userService.createUser(user, user.getPassword(), new ArrayList<Role>());
		redirectAttributes.addFlashAttribute("response_message", "User added/updated successfully!");
		
		return "redirect:/VITproject/admin/users";
	}
}
