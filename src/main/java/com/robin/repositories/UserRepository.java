package com.robin.repositories;

import org.springframework.data.repository.CrudRepository;

import com.robin.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);
	
	User findByEmail(String username);
}
