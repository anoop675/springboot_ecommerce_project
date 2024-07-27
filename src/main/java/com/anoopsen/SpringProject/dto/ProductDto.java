package com.anoopsen.SpringProject.dto;

import com.anoopsen.SpringProject.model.Category;

import jakarta.persistence.Column;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

@Data
public class ProductDto {
	
	private Long id;
	
	private String name;
	
	private int categoryId;
	
	private double price;
	
	private double weight;
	
	private String description;
	
	private String imageName;
}
