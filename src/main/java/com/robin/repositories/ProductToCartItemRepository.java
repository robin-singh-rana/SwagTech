package com.robin.repositories;

import org.springframework.data.repository.CrudRepository;

import com.robin.models.ProductToCartItem;

public interface ProductToCartItemRepository extends CrudRepository<ProductToCartItem, Long>{

}
