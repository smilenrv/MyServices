package com.cts.paymentservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

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

import com.cts.paymentservice.config.PaymentPropConfig;
import com.cts.paymentservice.dao.Customer;
import com.cts.paymentservice.exception.PaymentServiceException;
import com.cts.paymentservice.model.ApiResponse;
import com.cts.paymentservice.util.UtilityService;

class CustomerClientServiceTest {
	private PaymentPropConfig prop;
	private UtilityService util;
	@Mock
	private LoadBalancerClient loadBalancer;
	private CustomerClientService client;
	@Mock
	RestTemplate restTemplate;

	@BeforeEach
	void setUp() throws Exception {
		loadBalancer = Mockito.mock(LoadBalancerClient.class);
		prop = new PaymentPropConfig();
		prop.setFetchCustomer("/test/{id}");
		util = new UtilityService(loadBalancer);
		restTemplate = Mockito.mock(RestTemplate.class);
		client = new CustomerClientService(prop, util, restTemplate);
	}
	@Test
	void testGetCustomerUsingCustomerAPI() {
		ServiceInstance serviceInstance = new DefaultServiceInstance("test", "test", 8080, false);

		when(loadBalancer.choose(Mockito.anyString())).thenReturn(serviceInstance);
		when(restTemplate.exchange(Mockito.anyString(), Mockito.any(HttpMethod.class), Mockito.any(),
				Matchers.<ParameterizedTypeReference<ApiResponse<Customer>>>any(), Matchers.<Object>anyVararg())).thenReturn(
						ResponseEntity.ok(ApiResponse.<Customer>builder().data((Customer.builder().build())).build()));
		Customer customerList = client.getCustomerUsingCustomerAPI(1);
		assertThat(customerList).isNotNull();
	}

	@Test
	void testGetCustomerFallBack() {
		Assertions.assertThrows(PaymentServiceException.class, () -> {
			HttpClientErrorException ex = new HttpClientErrorException(
					org.springframework.http.HttpStatus.EXPECTATION_FAILED,
					"<error>200</error><errorMessage>gest</errorMessage>");
			client.getCustomerFallBack(1, ex);
		});
	}
	
	@Test
	void testGetCustomerFallBack1() {
		Assertions.assertThrows(PaymentServiceException.class, () -> {
			HttpClientErrorException ex = new HttpClientErrorException(
					org.springframework.http.HttpStatus.BAD_REQUEST,
					"<error>200</error><errorMessage>gest</errorMessage>");
			client.getCustomerFallBack(1, ex);
		});
	}
}
