package com.adminportal.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.adminportal.models.Product;
import com.adminportal.repositories.ProductRepository;


@Service
public class ProductService {
	
	@Autowired
	private ProductRepository productRepository;
	
	public Product save(Product product)
	{
		return productRepository.save(product);
	}

	public List<Product> findAll()
	{
		return (List<Product>)productRepository.findAll();
	}
	
}
