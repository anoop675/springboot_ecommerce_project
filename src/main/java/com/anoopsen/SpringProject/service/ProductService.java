package com.anoopsen.SpringProject.service;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import com.anoopsen.SpringProject.dto.ProductDto;
import com.anoopsen.SpringProject.model.Product;
import com.anoopsen.SpringProject.model.Product.ProductBuilder;
import com.anoopsen.SpringProject.repository.ProductRepo;
import com.anoopsen.SpringProject.dto.ProductDto.ProductDtoBuilder;

@Service
public class ProductService {
	
	
	@Autowired
	ProductRepo product_repository;
	
	@Autowired
	CategoryService category_service;
	
	private static final Logger logger = LoggerFactory.getLogger(ProductService.class);
	
	public static final String pathToProductImagesFolder = "/src/main/resources/static/productImages";
	
	public static String uploadDirectory = System.getProperty("user.dir") + pathToProductImagesFolder;
	
	//Alternate
	/*private String uploadDirectory;
	
	public ProductService() {
        try {
            File thisFile = new File(ResourceUtils.getURL("classpath:static/productImages").getPath()).getAbsoluteFile();
            this.uploadDirectory = thisFile.getAbsolutePath();
        } catch (FileNotFoundException e) {
            e.printStackTrace(); // Handle the exception as needed
        }
    }*/
	//File thisFile = new File(ResourceUtils.getURL("classpath:static/productImages").getPath()).getAbsoluteFile();
	//String uploadDirectory = thisFile.getAbsolutePath();
	
	public List<Product> getAllProduct(){
		return product_repository.findAll();
	}
	
	public ProductDto getProductById(int id){
		Product product = product_repository.findById(id).get(); 
		
		ProductDtoBuilder productDtoBuilder = ProductDto.builder()
														.id(product.getId())
														.name(product.getName())
														.categoryId(product.getCategory().getId())
														.price(product.getPrice())
														.weight(product.getWeight())
														.description(product.getDescription())
														.imageName(product.getImageName());
				  
		
		ProductDto productDto = productDtoBuilder.build();
		return productDto;
	}
	
	public Product get_productById(int id) {
		Optional<Product> product = product_repository.findById(id);
		
		return product.get();
	}
	
	public List<Product> getAllProductByCategoryId(int id){
		return product_repository.findAllByCategory_Id(id);
	}
	
	public ResponseEntity<String> addProduct(ProductDto productDto, MultipartFile file, String imgName) throws IOException {
		//copy attributes of ProductDto obj to Product obj
		String imageUUID;
		if(!file.isEmpty()) {
			imageUUID = file.getOriginalFilename();
			Path fileNameAndPath = Paths.get(uploadDirectory, imageUUID);
			Files.write(fileNameAndPath, file.getBytes());
		}
		else {
			imageUUID = imgName;
		}
		
		ProductBuilder productBuilder = Product.builder()
											   .id(productDto.getId())
											   .name(productDto.getName())
											   .category(
													   category_service.getCategoryById(
															   productDto.getCategoryId()
													   ).get())
											   .price(productDto.getPrice())
											   .weight(productDto.getWeight())
											   .description(productDto.getDescription())
											   .imageName(imageUUID);
		
		Product product = productBuilder.build();
		product_repository.save(product);
		return ResponseEntity.status(HttpStatus.OK).body("Product added successfully!");
	}
	
	public void removeProductById(int id) {
		product_repository.deleteById(id);
	}
}
