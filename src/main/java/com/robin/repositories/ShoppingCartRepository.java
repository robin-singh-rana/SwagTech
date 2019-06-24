package com.robin.repositories;

import org.springframework.data.repository.CrudRepository;

import com.robin.models.ShoppingCart;

public interface ShoppingCartRepository extends CrudRepository<ShoppingCart, Long>{

}
