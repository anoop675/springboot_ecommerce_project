package com.anoopsen.SpringProject.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.anoopsen.SpringProject.model.Product;
import com.anoopsen.SpringProject.repository.ProductRepo;

@Service
public class ProductService {
	
	@Autowired
	ProductRepo product_repository;
	
	public List<Product> getAllProduct(){
		return product_repository.findAll();
	}
	
	public void addProduct(Product product) {
		product_repository.save(product);
	}
	
	public void removeProductById(int id) {
		product_repository.deleteById(id);
	}
}
