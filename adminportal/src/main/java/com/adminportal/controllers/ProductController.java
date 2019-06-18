package com.adminportal.controllers;

import java.awt.image.BufferedImage;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.adminportal.models.Product;
import com.adminportal.services.ProductService;
import com.adminportal.utilities.ImageResizer;



@Controller
@RequestMapping("/product")
public class ProductController {

	@Autowired
	private ProductService productService;

	@RequestMapping(value = "/add", method = RequestMethod.GET)
	public String addProduct(Model model)
	{
		Product product=new Product();
		model.addAttribute("product",product);
		return "addproduct";
	}

	@RequestMapping(value = "/add", method = RequestMethod.POST)
	public String addProductPost(
			@ModelAttribute("product") Product product, HttpServletRequest request 
			) throws IOException
	{ 
		productService.save(product);

		MultipartFile productImage = product.getProductImage();

		//1. Converting multipartfile to file
		File convfile = new File(productImage.getOriginalFilename());
		convfile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convfile);
		fos.write(productImage.getBytes());
		fos.close();

		//2. resize here
		BufferedImage img = ImageIO.read(convfile);
		BufferedImage resizedImage = ImageResizer.resize(img,300,300);

		//3. converting buffered image to array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte bytes[];
		ImageIO.write(resizedImage, "png", baos);
		baos.flush();
		bytes = baos.toByteArray();
		baos.close();

		try
		{
			String name=product.getId()+ ".png";
			BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/others/images/product/"+ name)));
			stream.write(bytes);
			stream.close();
		} catch (Exception e)
		{
			e.printStackTrace();
		}

		return "redirect:productList";
	}

	@RequestMapping("/productList")
	public String productList(Model model)
	{
		List<Product> productList = (List<Product>) productService.findAll();
		model.addAttribute("productList",productList);
		return "productList";
	}

	@RequestMapping("/productInfo")
	public String productInfo(@RequestParam("id") Long id, Model model)
	{
		Optional<Product> product1 = productService.findById(id);
		Product product = product1.get();
		model.addAttribute("product",product);
		return "productInfo";
	}

	@RequestMapping("/updateProduct")
	public String updateProduct(@RequestParam("id") Long id, Model model)
	{
		Optional<Product> product1 = productService.findById(id);
		Product product = product1.get();
		model.addAttribute("product",product);
		return "updateProduct";
	}

	@RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
	public String updateProductList(@ModelAttribute("product") Product product, HttpServletRequest request) throws IOException
	{
		productService.save(product);

		MultipartFile productImage = product.getProductImage();

		//1. Converting multipartfile to file
		File convfile = new File(productImage.getOriginalFilename());
		convfile.createNewFile();
		FileOutputStream fos = new FileOutputStream(convfile);
		fos.write(productImage.getBytes());
		fos.close();

		//2. resize here
		BufferedImage img = ImageIO.read(convfile);
		BufferedImage resizedImage = ImageResizer.resize(img,300,300);

		//3. converting buffered image to array
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		byte bytes[];
		ImageIO.write(resizedImage, "png", baos);
		baos.flush();
		bytes = baos.toByteArray();
		baos.close();

		if(productImage.isEmpty()==false)
		{
			try {
				String name=product.getId()+ ".png";

				Files.delete(Paths.get("src/main/resources/static/others/images/product/"+name));

				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File("src/main/resources/static/others/images/product/"+ name)));
				stream.write(bytes);
				stream.close();
			} catch (Exception e)
			{
				e.printStackTrace();
			}
		}

		return "redirect:/product/productInfo?id="+product.getId();
	}

}
