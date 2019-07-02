package com.robin.services;

import java.util.ArrayList;
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

	public List<Product> findByCategory(String category) {
		
		List<Product> productList = productRepository.findByCategory(category);
		
		List<Product> activeProductList = new ArrayList<>();
		
		for(Product product:productList)
		{
			if(product.isActive())
				activeProductList.add(product);
		}
		return activeProductList;
	}

	public List<Product> findBySubCategory(String subcategory) {
		
		List<Product> productList = productRepository.findBySubcategory(subcategory);
		
		List<Product> activeProductList = new ArrayList<>();
		
		for(Product product:productList)
		{
			if(product.isActive())
				activeProductList.add(product);
		}
		return activeProductList;
	}

	public List<Product> findByBrand(String brand) {
		
		List<Product> productList = productRepository.findByBrand(brand);
		
		List<Product> activeProductList = new ArrayList<>();
		
		for(Product product:productList)
		{
			if(product.isActive())
				activeProductList.add(product);
		}
		return activeProductList;
	}
}
