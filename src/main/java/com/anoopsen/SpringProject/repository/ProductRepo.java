package com.anoopsen.SpringProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anoopsen.SpringProject.model.Product;

public interface ProductRepo extends JpaRepository<Product, Integer>{
	
}
