package com.anoopsen.SpringProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anoopsen.SpringProject.model.Product;

public interface ProductRepo extends JpaRepository<Product, Integer>{
	List<Product> findAllByCategory_Id(int id);
}
