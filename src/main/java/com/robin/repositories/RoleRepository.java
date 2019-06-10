package com.robin.repositories;

import org.springframework.data.repository.CrudRepository;

import com.robin.models.security.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{

	Role findByname(String name);
	
}
