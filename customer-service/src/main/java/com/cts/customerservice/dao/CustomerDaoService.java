package com.cts.customerservice.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import com.cts.customerservice.config.CustomerConfig;
import com.cts.customerservice.exception.CustomerNotFoundException;

@Component
public class CustomerDaoService {

	private CustomerRepository repository;
	private CustomerConfig prop;

	public CustomerDaoService(CustomerRepository repository, CustomerConfig prop) {
		this.repository = repository;
		this.prop = prop;
	}

	public List<Customer> getAllCustomers() {
		return repository.findAll();
	}

	public Boolean saveOrUpdateCustomer(Customer customer) {
		if (customer.getId() != null) {
			getCustomer(customer.getId());
		}
		repository.save(customer);
		return true;
	}

	public Customer getCustomer(int id) {
		return repository.findById(id).orElseThrow(() -> new CustomerNotFoundException(prop.getCustomerNotvailable()));
	}
}
