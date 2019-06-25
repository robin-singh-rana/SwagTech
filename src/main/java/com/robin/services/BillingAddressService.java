package com.robin.services;

import org.springframework.stereotype.Service;

import com.robin.models.BillingAddress;
import com.robin.models.UserBilling;

@Service
public class BillingAddressService {

	public BillingAddress setByUserBilling(UserBilling userBilling, BillingAddress billingAddress) {
		
		billingAddress.setBillingAddressName(userBilling.getUserBillingName());
		billingAddress.setBillingAddressStreet1(userBilling.getUserBillingStreet1());
		billingAddress.setBillingAddressStreet2(userBilling.getUserBillingStreet2());
		billingAddress.setBillingAddressCity(userBilling.getUserBillingCity());
		billingAddress.setBillingAddressState(userBilling.getUserBillingState());
		billingAddress.setBillingAddressCountry(userBilling.getUserBillingCountry());
		billingAddress.setBillingAddressZipcode(userBilling.getUserBillingZipcode());
		
		return billingAddress;
		
	}
}

