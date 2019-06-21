package com.robin.services;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.robin.models.UserShipping;
import com.robin.repositories.UserShippingRepository;

@Service
public class UserShippingService {

	@Autowired
	private UserShippingRepository userShippingRepository;
	
	public void removeById(Long id)
	{
		userShippingRepository.deleteById(id);
	}

	public Optional<UserShipping> findById(Long userShippingId) {
		
		return userShippingRepository.findById(userShippingId);
	}
}
