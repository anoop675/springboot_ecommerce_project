package com.anoopsen.SpringProject.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.anoopsen.SpringProject.model.Product;
import com.anoopsen.SpringProject.model.ProductRating;
import com.anoopsen.SpringProject.model.User;
import com.anoopsen.SpringProject.repository.RatingRepository;

@Service
public class RatingService {
	
	Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    RatingRepository ratingRepo;

    @Autowired
    ProductService productService;

    @Autowired
    UserService userService;

    public ResponseEntity<String> rateProduct(int productId, int rating) throws Exception {
    	
    	logger.info("rateProduct called");
        // Fetch the product and ensure it exists
        Product product = productService.get_productById(productId);
        if (product == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Product not found!");
        }
        
        // Get the authenticated user and ensure it's saved in the database
        User user = userService.getAuthenticatedUser();
        
        logger.info("product id:"+userService.getUser(user.getEmail()).getId());
        /*if (user == null) {
        	logger.info("user is not found");
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("User not authenticated or not saved.");
        }*/

        // Create a ProductRating instance and set values
        ProductRating productRating = new ProductRating();
        productRating.setProduct(product);
        productRating.setUser(userService.getUser(user.getEmail()));
        productRating.setRating(rating);

        logger.info("productRating of id:"+productRating.getId()+"for product id:"+product.getId()+" from user id:"+userService.getUser(user.getEmail()).getId()+" has rated:"+rating+" stars");
        // Save the rating and return a success response
        ratingRepo.save(productRating);
        logger.info("productRating of id:"+productRating.getId()+" is saved");
        return ResponseEntity.status(HttpStatus.OK).body("Product rated successfully!");
    }

    public double getAverageRating(int productId) {
    	Double avgRating = ratingRepo.findAverageRatingByProductId(productId);
        // Return 0.0 if there are no ratings or avgRating is null
        return (avgRating != null) ? avgRating : 0.0;
    }
}
