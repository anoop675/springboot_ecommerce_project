package com.anoopsen.SpringProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import com.anoopsen.SpringProject.model.Category;
import com.anoopsen.SpringProject.repository.CategoryRepo;

@Service
public class CategoryService {

	@Autowired
	CategoryRepo categoryRepo;
	
	public ResponseEntity<String> addCategory(Category category) {
		categoryRepo.save(category);
		return ResponseEntity.status(HttpStatus.OK).body("Category added/updated successfully!");
	}
	
	public List<Category> getAllCategory(){
		return categoryRepo.findAll();
	}
	
	public ResponseEntity<String> removeCategory(int id, ProductService productService) {
		if(!productService.getAllProductByCategoryId(id).isEmpty()) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Remove existing products under this category first");
		}
		else {
			categoryRepo.deleteById(id);
			return ResponseEntity.status(HttpStatus.OK).body("Category deleted successfully!");
		}
	}
	
	public Optional<Category> getCategoryById(int id) {
		return categoryRepo.findById(id);
	}
}
