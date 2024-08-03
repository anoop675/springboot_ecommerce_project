package com.anoopsen.SpringProject.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {
	
	private int id;
	
	private String name;
	
	private int categoryId;
	
	private double price;
	
	private double weight;
	
	private String description;
	
	private String imageName;
}
