package com.robin.repositories;

import org.springframework.data.repository.CrudRepository;

import com.robin.models.UserPayment;

public interface UserPaymentRepository extends CrudRepository<UserPayment, Long>{

}
