package com.robin.services;

import java.util.ArrayList;
import java.util.Optional;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.robin.models.ShoppingCart;
import com.robin.models.User;
import com.robin.models.UserBilling;
import com.robin.models.UserPayment;
import com.robin.models.UserShipping;
import com.robin.models.security.PasswordResetToken;
import com.robin.models.security.UserRole;
import com.robin.repositories.PasswordResetTokenRepository;
import com.robin.repositories.RoleRepository;
import com.robin.repositories.UserRepository;

@Service
public class UserService {

	private static final Logger LOG = LoggerFactory.getLogger(UserService.class);
	
	@Autowired
	private PasswordResetTokenRepository passwordResetTokenRepository;
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private RoleRepository roleRepository;
	
	public PasswordResetToken getPasswordResetToken(final String token)
	{
		return passwordResetTokenRepository.findByToken(token);
	}
	
	public void createPasswordResetTokenForUser(final User user, final String token)
	{
		final PasswordResetToken myToken =  new PasswordResetToken(token,user);
		passwordResetTokenRepository.save(myToken);
	}
	
	public User findByUsername(String username)
	{
		return userRepository.findByUsername(username);
	}
	
	public User findByEmail(String email)
	{
		return userRepository.findByEmail(email); 
	}
	
	// as there is transaction involved in db
	public User createUser(User user, Set<UserRole> userRoles) throws Exception
	{
		User localUser = userRepository.findByUsername(user.getUsername());
		
		if(localUser != null)
		{
			//throw new Exception("User already exists! Nothing will be done");
			LOG.info("user {} alredy exists. Nothing will be done.", user.getUsername());
		}
		else
		{
			//look through each user role
			for(UserRole ur:userRoles)
			{
				roleRepository.save(ur.getRole());
			}
		}
		
		user.getUserRoles().addAll(userRoles);
		
		ShoppingCart shoppingCart = new ShoppingCart();
		shoppingCart.setUser(user);
		user.setShoppingCart(shoppingCart);
		user.setUserShippingList(new ArrayList<UserShipping>());
		user.setUserPaymentList(new ArrayList<UserPayment>());
		
		localUser = userRepository.save(user);
		
		return localUser;
	}

	public User save(User user) {
		
		return userRepository.save(user);    //read crud repository for understanding these functions
		
	}

	public void updateUserBilling(UserBilling userBilling, UserPayment userPayment, User user)
	{
		userPayment.setUser(user);
		userPayment.setUserBilling(userBilling);
		userPayment.setDefaultPayment(true);
		
		userBilling.setUserPayment(userPayment);
		user.getUserPaymentList().add(userPayment);
		save(user);
	}

	public void updateUserShipping(UserShipping userShipping, User user) {
		userShipping.setUser(user);
		userShipping.setUserShippingDefault(true);
		user.getUserShippingList().add(userShipping);
		save(user);
	}

	public Optional<User> findById(Long id) {
		return userRepository.findById(id);
	}
	
}
