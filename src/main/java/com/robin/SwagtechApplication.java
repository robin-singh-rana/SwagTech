package com.robin;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.robin.models.User;
import com.robin.models.security.Role;
import com.robin.models.security.UserRole;
import com.robin.services.UserService;
import com.robin.utilities.SecurityUtility;

@SpringBootApplication
public class SwagtechApplication implements CommandLineRunner {

	@Autowired
	private	UserService userService;
	
	public static void main(String[] args) {
		SpringApplication.run(SwagtechApplication.class, args);
	}
	
	
	//just create a new user for testing as lot of times we need to register a new user
	//this user is regular role user
	@Override
	public void run(String... args) throws Exception
	{
		User user1=new User();
		user1.setFirstname("spring");
		user1.setLastname("boot");
		user1.setUsername("SB");
		user1.setPassword(SecurityUtility.passwordEncoder().encode("p"));
		user1.setEmail("yifiminubo@rockmailgroup.com");
		Set<UserRole> userRoles = new HashSet<>();
		Role role1=new Role();
		role1.setRoleId(1);
		role1.setName("ROLE_USER");
		userRoles.add(new UserRole(user1, role1));
		
		userService.createUser(user1, userRoles);
	}
}
