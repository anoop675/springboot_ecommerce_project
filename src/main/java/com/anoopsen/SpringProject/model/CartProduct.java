package com.anoopsen.SpringProject.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class CartProduct {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "cart_product_id", nullable = false, unique = true)
    private int id;
    
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cart_id", referencedColumnName = "cart_id")
    private Cart cart;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id", referencedColumnName = "product_id")
    private Product product;

    @Column(name = "product_quantity", nullable = false)
    public int quantity;
}