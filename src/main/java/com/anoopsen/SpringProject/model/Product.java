package com.anoopsen.SpringProject.model;

import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity(name="product_table")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="product_id", length=250, unique=true)
	private int id;
	
	@Column(name="product_name", length=200, unique=false)
	private String name;
	
	@ManyToOne(fetch=FetchType.LAZY)
	@JoinColumn(name="category_id", referencedColumnName="category_id")
	//@Column(name="product_category", length=200, unique=false)
	private Category category;
	
	@Column(name="product_price", length=200, unique=false)
	private double price;
	
	@Column(name="product_weight", length=200, unique=false)
	private double weight;
	
	@Column(name="product_description", length=1000, unique=false)
	private String description;
	
	@Column(name="product_image_name", length=250, unique=false)
	private String imageName;
	
	@ManyToMany(mappedBy = "products", cascade = CascadeType.MERGE, fetch = FetchType.EAGER) //FetchType.LAZY
    private List<Cart> carts;
}
