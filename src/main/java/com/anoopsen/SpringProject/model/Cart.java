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
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity(name="cart_table")
@Data
public class Cart {
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
	
	@Column(name = "cart_total", length = 200, nullable = false)
	private double total;
	
	@OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "user_id")
    private User user;

    @ManyToMany(cascade = CascadeType.MERGE, fetch = FetchType.EAGER)
    @JoinTable( // Creates the join table for Cart and Product
        name = "cart_product",
        joinColumns = {
            @JoinColumn(name = "cart_id", referencedColumnName = "cart_id")
        },
        inverseJoinColumns = {
            @JoinColumn(name = "product_id", referencedColumnName = "product_id")
        }
    )
    private List<Product> products;
}