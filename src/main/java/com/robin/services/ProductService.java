package com.robin.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robin.models.Product;
import com.robin.repositories.ProductRepository;

@Service
public class ProductService {

	@Autowired
	private ProductRepository productRepository;
	
	public List<Product> findAll()
	{
		return (List<Product>) productRepository.findAll();
	}

	public Optional<Product> findById(Long id) {
		
		return productRepository.findById(id);
	}
}
