package com.robin.controllers;

import java.security.Principal;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;
import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.robin.models.Product;
import com.robin.models.User;
import com.robin.models.UserBilling;
import com.robin.models.UserPayment;
import com.robin.models.UserShipping;
import com.robin.models.security.PasswordResetToken;
import com.robin.models.security.Role;
import com.robin.models.security.UserRole;
import com.robin.services.ProductService;
import com.robin.services.UserSecurityService;
import com.robin.services.UserService;
import com.robin.utilities.IndiaConstants;
import com.robin.utilities.MailConstructor;
import com.robin.utilities.SecurityUtility;

@Controller
public class HomeController {
	
	@Autowired
	private UserService userService; 
	
	@Autowired
	private UserSecurityService userSecurityService;
	
	@Autowired
	private ProductService productService;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private MailConstructor mailConstructor;
	
	@RequestMapping("/")
	public String home() {
		return "homepage";
	}
	
	@RequestMapping("/myaccount")
	public String account(Model model)
	{
		model.addAttribute("classActiveSignup",true);
		return "account";
	}
	
	@RequestMapping("/login")
	public String login(Model model)
	{
		model.addAttribute("classActiveLogin",true);
		return "account";
	}
	
	@RequestMapping("/forgetPassword")
	public String forgetpassword(
			HttpServletRequest request,
			@ModelAttribute("email") String email,
			Model model) throws Exception {
		
		User user = userService.findByEmail(email);
		
		//if this email doesn't exists
		if(user == null)
		{
			model.addAttribute("emailNotExist", true);
			return "account";
		}
		
		//else save user
		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		
		user.setPassword(encryptedPassword);
		
		userService.save(user);
		
		//upto here user is created
		
		//next we will generate token
		
		String token = UUID.randomUUID().toString(); // UUID is universal unique ID
		
		userService.createPasswordResetTokenForUser(user, token);
		
		String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		SimpleMailMessage newEmail = mailConstructor.constructResetTokenEmail(appUrl,request.getLocale(),token,user,password);
		
		mailSender.send(newEmail);
		
		model.addAttribute("forgetPasswordEmailSent",true);
		model.addAttribute("classActiveForgotPassword",true);
		
		return "account";
	}
	
	@RequestMapping(value = "/newUser", method = RequestMethod.POST)      // registration form logic 
	public String newUserPost(HttpServletRequest request,
			@ModelAttribute("email") String userEmail,
			@ModelAttribute("username") String username,
			Model model) throws Exception{
		model.addAttribute("email",userEmail);
		model.addAttribute("username",username);
		
		if(userService.findByUsername(username)!=null)  // if username already exists then dont proceed and return to myaccount by displaying a message
		{
			model.addAttribute("usernameExists",true);
			return "account";
		}
		
		if(userService.findByEmail(userEmail)!=null)  // if useremail already exists then dont proceed and return to myaccount by displaying a message
		{
			model.addAttribute("useremailExists",true);
			return "account";
		}
		
		// else create a new user
		
		User user = new User();
		
		user.setUsername(username);
		user.setEmail(userEmail);
		
		String password = SecurityUtility.randomPassword();
		String encryptedPassword = SecurityUtility.passwordEncoder().encode(password);
		
		user.setPassword(encryptedPassword);
		
		Role role = new Role();
		role.setRoleId(1);
		role.setName("ROLE_USER");
		
		Set<UserRole> userRoles = new HashSet<>();
		userRoles.add(new UserRole(user,role));
		userService.createUser(user,userRoles);
		
		//upto here user is created
		
		//next we will generate token
		
		String token = UUID.randomUUID().toString(); // UUID is universal unique ID
		
		userService.createPasswordResetTokenForUser(user, token);
		
		String appUrl = "http://"+request.getServerName()+":"+request.getServerPort()+request.getContextPath();
		
		SimpleMailMessage email = mailConstructor.constructResetTokenEmail(appUrl,request.getLocale(),token,user,password);
		
		mailSender.send(email);
		
		model.addAttribute("emailSent","true");
		
		return "account";
	}
	
	@RequestMapping("/newUser")   // basic controller to check the user
	public String newuser(Locale locale, @RequestParam("token") String token, Model model)
	{
		PasswordResetToken passToken = userService.getPasswordResetToken(token);
		
		if(passToken==null) // if passToken not found
		{
			String message="Invalid Token!";
			model.addAttribute("message",message);
			return "redirect:/badrequest";  // general error page
		}
		
		// ALL THIS CODE IS FOR ENSURING THAT CURRENT LOGGING SESSION IS FOR THIS RETRIEVED USER
		
		//if token is found then retrieve the user from that token
		User user = (User) passToken.getUser();
		String username = user.getUsername();
		
		//get user details from user security service
		UserDetails userDetails = userSecurityService.loadUserByUsername(username);
		
		//Then define Authentication environment using this user details and password
		Authentication authentication = new UsernamePasswordAuthenticationToken(userDetails, userDetails.getPassword(), userDetails.getAuthorities());
		
		SecurityContextHolder.getContext().setAuthentication(authentication);
		
		model.addAttribute("user",user);
		model.addAttribute("classActiveEdit",true);
		return "profile";
	}
	
	
	@RequestMapping("/viewProducts")
	public String viewProducts(Model model)
	{
		List<Product> productList = productService.findAll();
		model.addAttribute("productList",productList);
		return "viewProducts";
	}
	
	@RequestMapping("/productDetail")
	public String productDetail(
			@PathParam("id") Long id, Model model, Principal principal)
	{
		if(principal!=null)
		{
			String username=principal.getName();
			User user = userService.findByUsername(username);
			model.addAttribute(user);
		}
		
		Optional<Product> product1 = productService.findById(id);
		Product product = product1.get();
		
		model.addAttribute("product",product);
		
		List<Integer> qtyList = Arrays.asList(1,2,3,4,5,6,7,8,9,10);
		
		model.addAttribute("qtyList",qtyList);
		model.addAttribute("qty",1);
		
		return "productDetail";
		
	}
	
	@RequestMapping("/profile")
	public String profile(Model model, Principal principal)
	{
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user",user);
		
		model.addAttribute("userPaymentList",user.getUserPaymentList());
		model.addAttribute("userShippingList",user.getUserShippingList());
		//model.addAttribute("orderList",user.getOrderList());
		
		UserShipping userShipping = new UserShipping();
		model.addAttribute("userShipping",userShipping);
		
		model.addAttribute("listOfCreditCards",true);
		model.addAttribute("listOfShippingAddresses",true);
		
		List<String> stateList = IndiaConstants.listOfINDStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList",stateList);
		model.addAttribute("classActiveEdit",true);
		
		return "profile";
	}
	
	@RequestMapping("/listOfCreditCards")
	public String listOfCreditCards(Model model, Principal principal, HttpServletRequest request)
	{
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user",user);
		model.addAttribute("userPaymentList",user.getUserPaymentList());
		model.addAttribute("userShippingList",user.getUserShippingList());
		//model.addAttribute("orderList",user.orderList());
		
		model.addAttribute("listOfCreditCards",true);
		model.addAttribute("classActiveBilling",true);
		model.addAttribute("listOfShippingAddresses",true);
		
		return "profile";
	}
	
	@RequestMapping("/listOfShippingAddresses")
	public String listOfShippingAddresses(Model model, Principal principal, HttpServletRequest request)
	{
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user",user);
		model.addAttribute("userPaymentList",user.getUserPaymentList());
		model.addAttribute("userShippingList",user.getUserShippingList());
		//model.addAttribute("orderList",user.orderList());
		
		model.addAttribute("listOfCreditCards",true);
		model.addAttribute("classActiveBilling",true);
		model.addAttribute("listOfShippingAddresses",true);
		
		return "profile";
	}
	
	@RequestMapping("/addNewCreditCard")
	public String addNewCreditCard(Model model,Principal principal) 
	{
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user",user);
		
		model.addAttribute("addNewCreditCard",true);
		model.addAttribute("classActiveBilling",true);
		model.addAttribute("listOfShippingAddresses",true);
		
		UserBilling userBilling = new UserBilling();
		UserPayment userPayment = new UserPayment();
		
		model.addAttribute("userBilling",userBilling);
		model.addAttribute("userPayment",userPayment);
		
		List<String> stateList = IndiaConstants.listOfINDStatesName;
		Collections.sort(stateList);
		model.addAttribute("stateList",stateList);
		model.addAttribute("userPaymentList",user.getUserPaymentList());
		model.addAttribute("userShippingList",user.getUserShippingList());
		//model.addAttribute("orderList",user.orderList());
		
		return "profile";
	}
	
	@RequestMapping("/addNewShippingAddress")
	public String addNewShippingAddress(Model model,Principal principal) 
	{
		User user = userService.findByUsername(principal.getName());
		model.addAttribute("user",user);
		
		model.addAttribute("addNewShippingAddress",true);
		model.addAttribute("classActiveShipping",true);
		model.addAttribute("listOfCreditCards",true);
		
		UserShipping userShipping = new UserShipping();
		
		model.addAttribute("userShipping",userShipping);
		
		List<String> stateList = IndiaConstants.listOfINDStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList",stateList);
		model.addAttribute("userPaymentList",user.getUserPaymentList());
		model.addAttribute("userShippingList",user.getUserShippingList());
		//model.addAttribute("orderList",user.orderList());
		
		return "profile";
	}
	
	@RequestMapping(value = "/addNewCreditCard", method = RequestMethod.POST)
	public String addNewCreditCard(
			@ModelAttribute("userPayment") UserPayment userPayment,
			@ModelAttribute("userBilling") UserBilling userBilling,
			Principal principal, Model model)
	{
		User user = userService.findByUsername(principal.getName());
		userService.updateUserBilling(userBilling, userPayment, user);
		
		model.addAttribute("user",user);
		model.addAttribute("userPaymentList",user.getUserPaymentList());
		model.addAttribute("userShippingList",user.getUserShippingList());
		model.addAttribute("listOfCreditCards",true);
		model.addAttribute("classActiveBilling",true);
		model.addAttribute("listOfShippingAddresses",true);
		
		return "profile";
	}
	
}
