package com.cts.cardservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.cloud.client.DefaultServiceInstance;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.cts.cardservice.config.CardPropConfig;
import com.cts.cardservice.dao.Customer;
import com.cts.cardservice.exception.CardServiceException;
import com.cts.cardservice.model.ApiResponse;
import com.cts.cardservice.util.UtilityService;

class CustomerServiceClientTest {

	private CardPropConfig prop;
	private UtilityService util;
	@Mock
	private LoadBalancerClient loadBalancer;
	private CustomerServiceClient client;
	@Mock
	RestTemplate restTemplate;

	@BeforeEach
	void setUp() throws Exception {
		loadBalancer = Mockito.mock(LoadBalancerClient.class);
		prop = new CardPropConfig();
		prop.setFetchCustomers("test");
		prop.setFetchCustomer("/test/{id}");
		util = new UtilityService(loadBalancer);
		restTemplate = Mockito.mock(RestTemplate.class);
		client = new CustomerServiceClient(prop, util, restTemplate);
	}

	@Test
	void testGetCustomers() {
		ServiceInstance serviceInstance = new DefaultServiceInstance("test", "test", 8080, false);

		when(loadBalancer.choose(Mockito.anyString())).thenReturn(serviceInstance);
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(),
				Matchers.<ParameterizedTypeReference<ApiResponse<List<Customer>>>>any())).thenReturn(ResponseEntity.ok(
						ApiResponse.<List<Customer>>builder().data(Arrays.asList(Customer.builder().build())).build()));
		List<Customer> customerList = client.getCustomers();
		assertThat(customerList).isNotEmpty();
	}
	
	@Test
	void testGetCustomers_Null() {
		ServiceInstance serviceInstance = new DefaultServiceInstance("test", "test", 8080, false);

		when(loadBalancer.choose(Mockito.anyString())).thenReturn(serviceInstance);
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(),
				Matchers.<ParameterizedTypeReference<ApiResponse<List<Customer>>>>any())).thenReturn(ResponseEntity.ok(
						ApiResponse.<List<Customer>>builder().data(null).build()));
		List<Customer> customerList = client.getCustomers();
		assertThat(customerList).isEmpty();
	}
	
	@Test
	void testGetCustomers_error() {
		ServiceInstance serviceInstance = new DefaultServiceInstance("test", "test", 8080, false);

		when(loadBalancer.choose(Mockito.anyString())).thenReturn(serviceInstance);
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(),
				Matchers.<ParameterizedTypeReference<ApiResponse<List<Customer>>>>any())).thenReturn(ResponseEntity.ok(
						ApiResponse.<List<Customer>>builder().data(Arrays.asList(Customer.builder().build())).hasError(true).build()));
		List<Customer> customerList = client.getCustomers();
		assertThat(customerList).isEmpty();
	}

	@Test
	void testGetCustomer() {
		ServiceInstance serviceInstance = new DefaultServiceInstance("test", "test", 8080, false);

		when(loadBalancer.choose(Mockito.anyString())).thenReturn(serviceInstance);
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(),
				Matchers.<ParameterizedTypeReference<ApiResponse<Customer>>>any(), Matchers.<Object>anyVararg())).thenReturn(
						ResponseEntity.ok(ApiResponse.<Customer>builder().data((Customer.builder().build())).build()));
		Customer customerList = client.getCustomer(1);
		assertThat(customerList).isNotNull();
	}

	@Test
	void testGetCustomersFallBack() {
		Assertions.assertThrows(CardServiceException.class, () -> {
			client.getCustomerFallBack(1, new CardServiceException("test"));
		});
	}
	
	@Test
	void testGetCustomesrFallBack1() {
		Assertions.assertThrows(CardServiceException.class, () -> {
			client.getCustomersFallBack(new CardServiceException("test"));
		});
	}

	@Test
	void testGetCustomerFallBack() {
		Assertions.assertThrows(CardServiceException.class, () -> {
			HttpClientErrorException ex = new HttpClientErrorException(
					org.springframework.http.HttpStatus.EXPECTATION_FAILED,
					"<error>200</error><errorMessage>gest</errorMessage>");
			client.getCustomersFallBack(ex);
		});
	}

}
