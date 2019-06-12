package com.adminportal.repositories;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.models.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{
	
}
