package com.adminportal.services;

import java.util.List;
import java.util.Optional;

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
	
	public Optional<Product> findById(Long id)
	{
		return productRepository.findById(id);
	}

	public void removeById(Long id) {
		productRepository.deleteById(id);
	}
	
}
