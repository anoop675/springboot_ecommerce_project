package com.anoopsen.SpringProject.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anoopsen.SpringProject.model.Category;
import com.anoopsen.SpringProject.repository.CategoryRepo;

@Service
public class CategoryService {

	@Autowired
	CategoryRepo categoryRepo;
	
	public void addCategory(Category category) {
		categoryRepo.save(category);
	}
	
	public List<Category> getAllCategory(){
		return categoryRepo.findAll();
	}
	
	public void removeCategory(int id) {
		categoryRepo.deleteById(id);
	}
	
	public Optional<Category> getCategoryById(int id) {
		return categoryRepo.findById(id);
	}
}
