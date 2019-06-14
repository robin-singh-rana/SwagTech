package com.robin.repositories;

import org.springframework.data.repository.CrudRepository;

import com.robin.models.Product;

public interface ProductRepository extends CrudRepository<Product, Long>{

}
