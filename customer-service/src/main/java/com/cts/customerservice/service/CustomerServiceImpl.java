package com.cts.customerservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cts.customerservice.dao.Customer;
import com.cts.customerservice.dao.CustomerDaoService;
import com.cts.customerservice.model.CustomerRequest;

@Service
public class CustomerServiceImpl implements CustomerService {

	private CustomerDaoService dao;

	public CustomerServiceImpl(CustomerDaoService dao) {
		this.dao = dao;
	}

	public List<Customer> getAllCustomers() {
		return dao.getAllCustomers();
	}

	public Customer getCustomer(int id) {
		return dao.getCustomer(id);
	}

	public boolean createOrUpdateCustomer(CustomerRequest request, Integer id) {		
		return dao.saveOrUpdateCustomer(Customer.builder().address(request.getAddress()).age(Integer.valueOf(request.getAge()))
				.phoneNumber(request.getPhoneNumber()).emailAddress(request.getEmailAddress()).id(id)
				.name(request.getName()).build());
	}

}
