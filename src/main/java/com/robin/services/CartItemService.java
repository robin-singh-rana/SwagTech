package com.robin.services;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robin.models.CartItem;
import com.robin.models.Order;
import com.robin.models.Product;
import com.robin.models.ProductToCartItem;
import com.robin.models.ShoppingCart;
import com.robin.models.User;
import com.robin.repositories.CartItemRepository;
import com.robin.repositories.ProductToCartItemRepository;

@Service
public class CartItemService {

	@Autowired
	private CartItemRepository cartItemRepository;
	
	@Autowired
	ProductToCartItemRepository productToCartItemRepository;
	
	public List<CartItem> findByShoppingCart(ShoppingCart shoppingCart)
	{
		return cartItemRepository.findByShoppingCart(shoppingCart);
	}

	public CartItem updateCartItem(CartItem cartItem) {
		
		BigDecimal bigDecimal = new BigDecimal(cartItem.getProduct().getOurPrice()).multiply(new BigDecimal(cartItem.getQty()));
		
		bigDecimal.setScale(2, BigDecimal.ROUND_HALF_UP);
		cartItem.setSubtotal(bigDecimal);
		
		cartItemRepository.save(cartItem);
		
		return cartItem;
	}

	public CartItem addProductToCartItem(Product product, User user, int qty) {
		List<CartItem> cartItemList = findByShoppingCart(user.getShoppingCart());
		
		for(CartItem cartItem:cartItemList)
		{
			//if product is already in shopping cart
			if(product.getId() == cartItem.getProduct().getId())
			{
				cartItem.setQty(cartItem.getQty()+qty);
				cartItem.setSubtotal(new BigDecimal(product.getOurPrice()).multiply(new BigDecimal(qty)));;
				cartItemRepository.save(cartItem);
				return cartItem;
			}
		}
		
		//if product is not in the shopping cart
		CartItem cartItem = new CartItem();
		cartItem.setShoppingCart(user.getShoppingCart());
		cartItem.setProduct(product);
		cartItem.setQty(qty);
		cartItem.setSubtotal(new BigDecimal(product.getOurPrice()).multiply(new BigDecimal(qty)));;
		cartItem = cartItemRepository.save(cartItem);
		
		ProductToCartItem productToCartItem = new ProductToCartItem();
		productToCartItem.setProduct(product);
		productToCartItem.setCartItem(cartItem);
		productToCartItemRepository.save(productToCartItem);
		
		return cartItem;
	}

	public Optional<CartItem> findById(Long cartItemId) {
		return cartItemRepository.findById(cartItemId);
	}

	public void removeCartItem(Optional<CartItem> optional) {
		CartItem cartItem = optional.get();
		productToCartItemRepository.deleteByCartItem(cartItem);
		cartItemRepository.delete(cartItem);
	}

	public CartItem save(CartItem cartItem) {
		return cartItemRepository.save(cartItem);
	}

	public List<CartItem> findByOrder(Order order) {
		return cartItemRepository.findByOrder(order);
	}
	
}
