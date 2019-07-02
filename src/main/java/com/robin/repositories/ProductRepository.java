package com.robin.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.robin.models.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{

	List<Product> findByCategory(String category);

	List<Product> findBySubcategory(String subcategory);
	
	List<Product> findByBrand(String brand);

	List<Product> findByTitleContaining(String title);

	List<Product> findByCategoryAndOurPriceLessThan(String category,double listprice);

	List<Product> findByCategoryAndBrand(String category, String brand);

}
