package com.robin.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robin.models.UserPayment;
import com.robin.repositories.UserPaymentRepository;

@Service
public class UserPaymentService {

	@Autowired
	private UserPaymentRepository userPaymentrepository;
	
	public Optional<UserPayment> findById(Long id)
	{
		return userPaymentrepository.findById(id); 
	}

	public void removeById(Long creditCardId) {
		userPaymentrepository.deleteById(creditCardId);
	}
}
