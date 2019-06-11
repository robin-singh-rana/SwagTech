package com.adminportal.repositories;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.models.User;

public interface UserRepository extends CrudRepository<User, Long> {

	User findByUsername(String username);
	
	User findByEmail(String username);
}
