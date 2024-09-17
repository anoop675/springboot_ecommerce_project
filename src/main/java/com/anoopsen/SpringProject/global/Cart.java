//TODO: Make DB Table and define Cart schema
package com.anoopsen.SpringProject.global;

import java.util.ArrayList;
import java.util.List;

import com.anoopsen.SpringProject.model.Product;

public class Cart {
	public static List<Product> cart;  //Cart is kept has a global data, as cart count should be rendered in all pages
	static {
		cart = new ArrayList<Product>();
	}
}
