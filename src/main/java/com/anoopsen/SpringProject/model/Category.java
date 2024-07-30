package com.anoopsen.SpringProject.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Data;

@Entity(name = "Category_table")
@Data
public class Category {
	
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "category_id",
			/*length = 200,*/
			unique = true
			)
	private int id;
	
	@Column(name = "category_name",
			length = 200,
			unique = false
			)
	private String name;
}
