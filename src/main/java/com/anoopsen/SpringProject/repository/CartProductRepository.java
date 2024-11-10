package com.anoopsen.SpringProject.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.anoopsen.SpringProject.model.CartProduct;

@Repository
public interface CartProductRepository extends JpaRepository<CartProduct, Integer> {
    // List<CartProduct> findByCart(Cart cart);
	
	 @Query("SELECT cp.product, cp.quantity FROM CartProduct cp WHERE cp.cart.id = :cartId") //this will help get the quantity of product in the cart
	 List<Object[]> findProductWithQuantityByCartId(@Param("cartId") int cartId);
	 
	 @Modifying
	 @Query("DELETE FROM CartProduct cp WHERE cp.id = :id")
	 void deleteCartProductById(@Param("id") int id);
	 
	 @Modifying
	 @Query("DELETE FROM CartProduct cp WHERE cp.cart.id = :cartId")
	 void deleteByCartId(@Param("cartId") int cartId);
	 
	 @Modifying
	 @Query("DELETE FROM CartProduct cp WHERE cp.product.id = :productId")
	 void deleteByProductId(@Param("productId") int productId);
}

