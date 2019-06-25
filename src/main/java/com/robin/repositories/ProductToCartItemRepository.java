package com.robin.repositories;

import org.springframework.data.repository.CrudRepository;
import org.springframework.transaction.annotation.Transactional;

import com.robin.models.CartItem;
import com.robin.models.ProductToCartItem;

@Transactional
public interface ProductToCartItemRepository extends CrudRepository<ProductToCartItem, Long>{

	void deleteByCartItem(CartItem cartItem);
	
}
