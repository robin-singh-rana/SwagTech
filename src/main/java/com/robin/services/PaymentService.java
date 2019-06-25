package com.robin.services;

import org.springframework.stereotype.Service;

import com.robin.models.Payment;
import com.robin.models.UserPayment;

@Service
public class PaymentService {

	public Payment setByUserShipping(UserPayment userPayment, Payment payment) {
		payment.setType(userPayment.getType());
		payment.setHolderName(userPayment.getHolderName());
		payment.setCardNumber(userPayment.getCardNumber());
		payment.setCardName(userPayment.getCardName());
		payment.setExpiryMonth(userPayment.getExpiryMonth());
		payment.setCvc(userPayment.getCvc());
		payment.setType(userPayment.getType());
		
		return payment;
	}

}
