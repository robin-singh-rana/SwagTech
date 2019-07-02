package com.robin.controllers;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.robin.models.Product;
import com.robin.models.User;
import com.robin.services.ProductService;
import com.robin.services.UserService;

@Controller
public class SearchController {
	
	@Autowired
	private UserService userService;
	
	@Autowired
	private ProductService productService;

	@RequestMapping("/searchByCategory")
	public String searchbycategory(
			@RequestParam("category") String category,
			Model model, Principal principal)
	{
		if(principal!=null)
		{
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user",user);
		}
		
		List<Product> productList = productService.findByCategory(category);
		
		if(productList.isEmpty())
		{
			model.addAttribute("emptyList",true);
			return "viewProducts";
		}
		
		model.addAttribute("productList",productList);
		return "viewProducts";
	}
	
	@RequestMapping("/searchBySubCategory")
	public String searchbysubcategory(
			@RequestParam("subcategory") String subcategory,
			Model model, Principal principal)
	{
		if(principal!=null)
		{
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user",user);
		}
		
		List<Product> productList = productService.findBySubCategory(subcategory);
		
		if(productList.isEmpty())
		{
			model.addAttribute("emptyList",true);
			return "viewProducts";
		}
		
		model.addAttribute("productList",productList);
		return "viewProducts";
	}
	
	@RequestMapping("/searchByBrand")
	public String searchbybrand(
			@RequestParam("brand") String brand,
			Model model, Principal principal)
	{
		if(principal!=null)
		{
			String username = principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute("user",user);
		}
		
		List<Product> productList = productService.findByBrand(brand);
		
		if(productList.isEmpty())
		{
			model.addAttribute("emptyList",true);
			return "viewProducts";
		}
		
		model.addAttribute("productList",productList);
		return "viewProducts";
	}
	
}
