package com.adminportal.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.adminportal.models.Product;

@Controller
@RequestMapping("/product")
public class ProductController {

	@RequestMapping("/add")
	public String addProduct(Model model)
	{
		Product product=new Product();
		model.addAttribute("product",product);
		return "addproduct";
	}
	
}
