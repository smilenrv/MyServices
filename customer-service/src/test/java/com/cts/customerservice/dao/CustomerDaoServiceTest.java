package com.cts.customerservice.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.cts.customerservice.config.CustomerConfig;
import com.cts.customerservice.exception.CustomerNotFoundException;

class CustomerDaoServiceTest {

	private CustomerDaoService dao;

	@Mock
	private CustomerRepository repository;
	@Mock
	private CustomerConfig prop;

	@BeforeEach
	void setUp() throws Exception {
		repository = Mockito.mock(CustomerRepository.class);
		prop = Mockito.mock(CustomerConfig.class);
		dao = new CustomerDaoService(repository, prop);
	}

	@Test
	void testGetAllCustomers_with_valid_Customer() {
		when(repository.findAll()).thenReturn(
				Arrays.asList(Customer.builder().name("TEST").build(), Customer.builder().name("TEST1").build()));
		List<Customer> customerList = dao.getAllCustomers();
		assertThat(customerList).isNotEmpty();
		assertThat(customerList.size()).isEqualTo(2);
		verify(repository).findAll();
	}

	@Test
	void test_fetchAllCustomer_emptyList() {
		when(repository.findAll()).thenReturn(Collections.emptyList());
		List<Customer> customerList = dao.getAllCustomers();
		assertThat(customerList).isEmpty();
		assertThat(customerList.size()).isEqualTo(0);
		verify(repository).findAll();
	}

	@Test
	void testUpdateCustomer() {
		when(repository.findById(Mockito.anyInt()))
				.thenReturn(java.util.Optional.of(Customer.builder().name("TEST").id(1).build()));
		boolean updated = dao.saveOrUpdateCustomer(Mockito.mock(Customer.class));
		assertThat(updated).isTrue();
	}

	@Test
	void testSaveCustomer() {
		boolean updated = dao.saveOrUpdateCustomer(Customer.builder().build());
		assertThat(updated).isTrue();
	}

	@Test
	void test_fetch_specific_Customer() {
		when(repository.findById(Mockito.anyInt()))
				.thenReturn(java.util.Optional.of(Customer.builder().name("TEST").id(1).build()));
		Customer customer = dao.getCustomer(1);
		assertThat(customer).isNotNull();
		assertThat(customer.getName()).isEqualTo("TEST");
	}

	@Test
	void test_nocustomer_found_throw_exception() {
		Card card = new Card();
		card.setCustomer(Customer.builder().id(1).build());
		card.getCustomerId();
		when(repository.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		when(prop.getCustomerNotvailable()).thenReturn("Customer is not present");
		Assertions.assertThrows(CustomerNotFoundException.class, () -> {
			dao.getCustomer(1);
		});
	}

}
