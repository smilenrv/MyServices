package com.cts.paymentservice.service;

import java.util.List;

import com.cts.paymentservice.dao.Payment;
import com.cts.paymentservice.model.PaymentRequest;

public interface PaymentService {
	
	boolean initiatePaymentTransaction (Integer customerId, PaymentRequest requeset);
	
	List<Payment> getPayments (Integer customerId);

}
