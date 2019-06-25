package com.robin.controllers;

import java.security.Principal;
import java.util.Collections;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.robin.models.BillingAddress;
import com.robin.models.CartItem;
import com.robin.models.Payment;
import com.robin.models.ShippingAddress;
import com.robin.models.ShoppingCart;
import com.robin.models.User;
import com.robin.models.UserPayment;
import com.robin.models.UserShipping;
import com.robin.services.BillingAddressService;
import com.robin.services.CartItemService;
import com.robin.services.PaymentService;
import com.robin.services.ShippingAddressService;
import com.robin.services.UserService;
import com.robin.utilities.IndiaConstants;

@Controller
public class CheckoutController {

	// type of global variables
	private ShippingAddress shippingAddress = new ShippingAddress();
	private BillingAddress billingAddress = new BillingAddress();
	private Payment payment = new Payment();

	@Autowired
	private UserService userService;

	@Autowired
	private CartItemService cartItemService;

	@Autowired
	private ShippingAddressService shippingAddressService; 
	
	@Autowired
	private BillingAddressService billingAddressService; 
	
	@Autowired
	private PaymentService paymentService; 

	@RequestMapping("/checkout")
	public String checkout(
			@RequestParam("id") Long cartId,
			@RequestParam(value = "missingRequiredField", required = false) boolean missingRequiredField,
			Model model, Principal principal)
	{
		User user = userService.findByUsername(principal.getName());

		if(cartId != user.getShoppingCart().getId())
			return "badRequestPage";

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(user.getShoppingCart());
		
		// then check the size
		/*if(cartItemList.size()==0)
		{
			model.addAttribute("emptyCart",true);
			return "redirect:/shoppingCart/cart";
		}

		//each item in the cart is in stock
		for(CartItem cartItem : cartItemList)
		{
			if(cartItem.getProduct().getInStockNumber() < cartItem.getQty())
				model.addAttribute("notEnoughStock",true);
			return "redirect:/shoppingCart/cart";
		}*/

		List<UserShipping> userShippingList = user.getUserShippingList();
		List<UserPayment> userPaymentList = user.getUserPaymentList();
		model.addAttribute("userShippingList",userShippingList);
		model.addAttribute("userPaymentList",userPaymentList);

		if(userShippingList.size()==0)
		{
			model.addAttribute("emptyShippingList",true);
		}else {
			model.addAttribute("emptyShippingList",false);
		}

		if(userPaymentList.size()==0)
		{
			model.addAttribute("emptyPaymentList",true);
		}else {
			model.addAttribute("emptyPaymentList",false);
		}

		ShoppingCart shoppingCart = user.getShoppingCart();

		//set default shippingaddress to shipping content
		for(UserShipping userShipping: userShippingList)
		{
			if(userShipping.isUserShippingDefault())
			{
				shippingAddressService.setByUserShipping(userShipping, shippingAddress);
			}
		}

		//set default payment to shipping content
		for(UserPayment userPayment: userPaymentList)
		{
			if(userPayment.isDefaultPayment())
			{
				paymentService.setByUserShipping(userPayment, payment);
				billingAddressService.setByUserBilling(userPayment.getUserBilling(), billingAddress);
			}
		}
		
		model.addAttribute("billingAddress",billingAddress);
		model.addAttribute("shippingAddress",shippingAddress);
		model.addAttribute("payment",payment);
		model.addAttribute("cartItemList",cartItemList);
		model.addAttribute("shoppingCart",user.getShoppingCart());
		
		List<String> stateList = IndiaConstants.listOfINDStatesCode;
		Collections.sort(stateList);
		model.addAttribute("stateList",stateList);
		model.addAttribute("classActiveShipping",true);
		
		if(missingRequiredField == true)
		{
			model.addAttribute("missingRequiredField",true);
		}
		
		return "checkout";
	}

}
