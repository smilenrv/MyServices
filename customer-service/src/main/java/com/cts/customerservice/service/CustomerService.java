package com.cts.customerservice.service;

import java.util.List;

import com.cts.customerservice.dao.Customer;
import com.cts.customerservice.model.CustomerRequest;

public interface CustomerService {
	List<Customer> getAllCustomers();

	Customer getCustomer(int id);
	
	boolean createOrUpdateCustomer(CustomerRequest request, Integer id);
}
