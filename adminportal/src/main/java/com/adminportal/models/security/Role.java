package com.adminportal.models.security;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class Role {
	
	@Id
	private int roleId;
	private String name;
	
	
	/*This onetomany specifies the relationship between Role class and UserRole class i.e
	  one Role can have many UserRoles. As in UserRole we are specifying the relationship b/w 
	  User and Roles and one Role can be assigned to different Users.
	  [ONE ROLE CAN HAVE MAPPING TO MULTIPLE USERROLES] */
	@OneToMany(mappedBy = "role", cascade=CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<UserRole> userRoles = new HashSet<>();


	public int getRoleId() {
		return roleId;
	}


	public void setRoleId(int roleId) {
		this.roleId = roleId;
	}


	public String getName() {
		return name;
	}


	public void setName(String name) {
		this.name = name;
	}


	public Set<UserRole> getUserRoles() {
		return userRoles;
	}


	public void setUserRoles(Set<UserRole> userRoles) {
		this.userRoles = userRoles;
	}
	
}
