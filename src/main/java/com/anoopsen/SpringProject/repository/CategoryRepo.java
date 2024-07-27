package com.anoopsen.SpringProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anoopsen.SpringProject.model.Category;

public interface CategoryRepo extends JpaRepository<Category, Integer>{
	//---Managed in CategoryService
}
