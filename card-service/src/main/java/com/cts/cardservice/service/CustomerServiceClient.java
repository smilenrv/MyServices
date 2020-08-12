package com.cts.cardservice.service;

import java.util.Collections;
import java.util.List;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cts.cardservice.config.CardPropConfig;
import com.cts.cardservice.dao.Customer;
import com.cts.cardservice.exception.CardServiceException;
import com.cts.cardservice.model.ApiResponse;
import com.cts.cardservice.util.UtilityService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RibbonClient(name="customer-service")
public class CustomerServiceClient {

	private RestTemplate restTemplate;
	private CardPropConfig prop;
	private UtilityService util;

	public CustomerServiceClient(CardPropConfig prop, UtilityService util) {
		this.restTemplate = new RestTemplate();
		this.prop = prop;
		this.util = util;
	}

	@HystrixCommand(fallbackMethod = "getCustomersFallBack", commandKey = "customer-api")
	public List<Customer> getCustomers() {
		log.info("fetch customers");
		ApiResponse<List<Customer>> response = restTemplate.exchange(util.getServiceUrl("customer-service") + prop.getFetchCustomers(),
				HttpMethod.GET, null, new ParameterizedTypeReference<ApiResponse<List<Customer>>>() {
				}).getBody();
		log.info("Get customers API Response {}", response);
		List<Customer> list = response.getData();
		return list != null && !response.isHasError() ? list : Collections.emptyList();
	}

	@HystrixCommand(fallbackMethod = "getCustomerFallBack", commandKey = "customer-api")
	public Customer getCustomer(Integer customerId) {
		log.info("fetch customer");
		ApiResponse<Customer> response = restTemplate.exchange(util.getServiceUrl("customer-service") + prop.getFetchCustomer(),
				HttpMethod.GET, null, new ParameterizedTypeReference<ApiResponse<Customer>>() {
				}, customerId).getBody();
		log.info("Get customer API Response {}", response);
		return (Customer) response.getData();
	}

	public List<Customer> getCustomersFallBack(Throwable e) {
		log.info("Error in fetching customer details from customer API " + e);
		util.parseExceptionAndThrowIfExists(e);
		throw new CardServiceException(prop.getCustomerApiNotAvailable());
	}

	public Customer getCustomerFallBack(Integer customerId, Throwable e) {
		log.info("Error in fetching customer details from customer API");
		util.parseExceptionAndThrowIfExists(e);
		throw new CardServiceException(prop.getCustomerApiNotAvailable());
	}
}
