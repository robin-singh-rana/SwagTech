package com.robin.repositories;

import org.springframework.data.repository.CrudRepository;

import com.robin.models.Order;

public interface OrderRepository extends CrudRepository<Order, Long>{

}
