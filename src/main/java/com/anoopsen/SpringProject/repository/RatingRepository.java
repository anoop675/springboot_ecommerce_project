package com.anoopsen.SpringProject.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.anoopsen.SpringProject.model.Category;
import com.anoopsen.SpringProject.model.ProductRating;

public interface RatingRepository extends JpaRepository<ProductRating, Integer>{
	// Query to calculate average rating for a product by product ID
    @Query("SELECT AVG(pr.rating) FROM ProductRating pr WHERE pr.product.id = :productId")
    Double findAverageRatingByProductId(@Param("productId") int productId);
    
    @Modifying
    @Query("DELETE FROM ProductRating pr WHERE pr.product.id = :productId")
    void deleteByProductId(@Param("productId") int productId);
}
