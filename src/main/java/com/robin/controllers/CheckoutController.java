package com.robin.controllers;

import java.security.Principal;
import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import com.robin.models.BillingAddress;
import com.robin.models.CartItem;
import com.robin.models.Order;
import com.robin.models.Payment;
import com.robin.models.ShippingAddress;
import com.robin.models.ShoppingCart;
import com.robin.models.User;
import com.robin.models.UserPayment;
import com.robin.models.UserShipping;
import com.robin.services.BillingAddressService;
import com.robin.services.CartItemService;
import com.robin.services.OrderService;
import com.robin.services.PaymentService;
import com.robin.services.ShippingAddressService;
import com.robin.services.ShoppingCartService;
import com.robin.services.UserService;
import com.robin.utilities.IndiaConstants;
import com.robin.utilities.MailConstructor;

@Controller
public class CheckoutController {

	// type of global variables
	private ShippingAddress shippingAddress = new ShippingAddress();
	private BillingAddress billingAddress = new BillingAddress();
	private Payment payment = new Payment();

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private MailConstructor mailConstructor;
	
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

	@Autowired
	private OrderService orderService;
	
	@Autowired
	private ShoppingCartService shoppingCartService;
	
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
		model.addAttribute("shoppingCart",shoppingCart);

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

	@RequestMapping(value = "/checkout", method = RequestMethod.POST)
	public String CheckPost(
			@ModelAttribute("shippingAddress") ShippingAddress shippingAddress,
			@ModelAttribute("billingAddress") BillingAddress billingAddress,
			@ModelAttribute("payment") Payment payment,
			@ModelAttribute("shippingMethod") String shippingMethod,
			Principal principal, Model model)
	{
		//1. Retreive shoppingCart
		ShoppingCart shoppingCart = userService.findByUsername(principal.getName()).getShoppingCart();

		List<CartItem> cartItemList = cartItemService.findByShoppingCart(shoppingCart);
		model.addAttribute("cartItemList",cartItemList);
		
		billingAddress.setBillingAddressName(shippingAddress.getShippingAddressName());
		billingAddress.setBillingAddressStreet1(shippingAddress.getShippingAddressStreet1());
		billingAddress.setBillingAddressStreet2(shippingAddress.getShippingAddressStreet2());
		billingAddress.setBillingAddressCity(shippingAddress.getShippingAddressCity());
		billingAddress.setBillingAddressState(shippingAddress.getShippingAddressState());
		billingAddress.setBillingAddressCountry(shippingAddress.getShippingAddressCountry());
		billingAddress.setBillingAddressZipcode(shippingAddress.getShippingAddressZipcode());

		if (shippingAddress.getShippingAddressStreet1().isEmpty() || shippingAddress.getShippingAddressCity().isEmpty()
				|| shippingAddress.getShippingAddressState().isEmpty()
				|| shippingAddress.getShippingAddressName().isEmpty()
				|| shippingAddress.getShippingAddressZipcode().isEmpty() || payment.getCardNumber().isEmpty()
				|| payment.getCvc() == 0 || billingAddress.getBillingAddressStreet1().isEmpty()
				|| billingAddress.getBillingAddressCity().isEmpty() || billingAddress.getBillingAddressState().isEmpty()
				|| billingAddress.getBillingAddressName().isEmpty()
				|| billingAddress.getBillingAddressZipcode().isEmpty())
			return "redirect:/checkout?id=" + shoppingCart.getId() + "&missingRequiredField=true";

		User user = userService.findByUsername(principal.getName());

		Order order = orderService.createOrder(shoppingCart, shippingAddress, billingAddress, payment, shippingMethod, user);

		mailSender.send(mailConstructor.constructOrderConfirmationEmail(user, order, Locale.ENGLISH));

		shoppingCartService.clearShoppingCart(shoppingCart);

		LocalDate today = LocalDate.now();
		LocalDate estimatedDeliveryDate;

		if (shippingMethod.equals("groundShipping")) {
			estimatedDeliveryDate = today.plusDays(5);
		} else {
			estimatedDeliveryDate = today.plusDays(3);
		}

		model.addAttribute("estimatedDeliveryDate", estimatedDeliveryDate);

		return "orderSubmittedPage";
	}

}
