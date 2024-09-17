package com.anoopsen.SpringProject.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.anoopsen.SpringProject.model.Cart;
import com.anoopsen.SpringProject.model.User;

public interface CartRepo extends JpaRepository<Cart, Integer>{
	Optional<Cart> findByUser(String email); //User user
}
