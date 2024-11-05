package com.anoopsen.SpringProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anoopsen.SpringProject.model.Category;
import com.anoopsen.SpringProject.model.ProductRating;

public interface RatingRepository extends JpaRepository<ProductRating, Integer>{
	
}
