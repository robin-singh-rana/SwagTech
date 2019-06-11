package com.adminportal.repositories;

import org.springframework.data.repository.CrudRepository;

import com.adminportal.models.security.Role;

public interface RoleRepository extends CrudRepository<Role, Long>{

	Role findByname(String name);
	
}
