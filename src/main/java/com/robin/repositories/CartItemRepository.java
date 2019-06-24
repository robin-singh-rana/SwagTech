package com.robin.repositories;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import com.robin.models.CartItem;
import com.robin.models.ShoppingCart;

public interface CartItemRepository extends CrudRepository<CartItem, Long>{

	List<CartItem> findByShoppingCart(ShoppingCart shoppingCart);

}
