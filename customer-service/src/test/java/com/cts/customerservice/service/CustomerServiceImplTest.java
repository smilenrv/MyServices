package com.cts.customerservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.cts.customerservice.dao.Customer;
import com.cts.customerservice.dao.CustomerDaoService;
import com.cts.customerservice.model.CustomerRequest;

@RunWith(JUnit4.class)
class CustomerServiceImplTest {

	@Mock
	private CustomerDaoService dao;

	private CustomerServiceImpl service;

	@BeforeEach
	public void setup() {
		dao = Mockito.mock(CustomerDaoService.class);
		service = new CustomerServiceImpl(dao);
	}

	@Test
	void testGetAllCustomers_empty() {
		List<Customer> customerList = service.getAllCustomers();
		assertThat(customerList).isEmpty();
	}

	@Test
	void testGetAllCustomers_CustomerList() {
		when(dao.getAllCustomers()).thenReturn(
				Arrays.asList(Customer.builder().name("TEST").build(), Customer.builder().name("TEST1").build()));
		List<Customer> customerList = service.getAllCustomers();
		assertThat(customerList).isNotEmpty();
		assertThat(customerList.size()).isEqualTo(2);
		verify(dao).getAllCustomers();
	}

	@Test
	void testGetCustomer_NullOrEmpty() {
		Customer customer = service.getCustomer(1);
		assertThat(customer).isNull();
	}

	@Test
	void testGetCustomer_NotNull() {
		when(dao.getCustomer(Mockito.anyInt())).thenReturn(Customer.builder().name("TEST").id(1).build());
		Customer customer = service.getCustomer(1);
		assertThat(customer).isNotNull();
		assertThat(customer.getName()).isEqualTo("TEST");
		verify(dao).getCustomer(1);
	}

	@Test
	void testCreateOrUpdateCustomer() {
		CustomerRequest request = new CustomerRequest();
		request.setName("test");
		request.setAge("1");
		request.setAddress("test");
		request.setPhoneNumber("1234");
		request.setEmailAddress("test@email.com");
		when(dao.saveOrUpdateCustomer(Mockito.any(Customer.class))).thenReturn(true);
		boolean updated = service.createOrUpdateCustomer(request, 1);
		assertEquals(true, updated);
	}

	@Test
	void testCreateOrUpdateCustomer_False() {
		CustomerRequest request = new CustomerRequest();
		request.setAge("1");
		boolean updated = service.createOrUpdateCustomer(request, 1);
		assertEquals(false, updated);
	}

	@Test
	void testCreateOrUpdateCustomer_Exception() {
		CustomerRequest request = new CustomerRequest();
		Assertions.assertThrows(NumberFormatException.class, () -> {
			service.createOrUpdateCustomer(request, 1);
		});
	}

}
