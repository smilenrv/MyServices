package com.cts.paymentservice.service;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cts.paymentservice.config.PaymentPropConfig;
import com.cts.paymentservice.dao.Customer;
import com.cts.paymentservice.exception.PaymentServiceException;
import com.cts.paymentservice.model.ApiResponse;
import com.cts.paymentservice.util.UtilityService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RibbonClient(name = "customer-service")
public class CustomerClientService {

	private RestTemplate restTemplate;
	private PaymentPropConfig prop;
	private UtilityService util;

	public CustomerClientService(PaymentPropConfig prop, UtilityService util, @Qualifier(value = "restTemplate") RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
		this.prop = prop;
		this.util = util;
	}

	@HystrixCommand(fallbackMethod = "getCustomerFallBack", commandKey = "customer-api")
	public Customer getCustomerUsingCustomerAPI(Integer customerId) {
		String service = util.getServiceUrl("customer-service");
		log.info("fetch customer " + service);
		ApiResponse<Customer> response = restTemplate.exchange(service + prop.getFetchCustomer(), HttpMethod.GET, null,
				new ParameterizedTypeReference<ApiResponse<Customer>>() {
				}, customerId).getBody();
		log.info("Get customer API Response {}", response);
		return response.getData();
	}

	public Customer getCustomerFallBack(Integer customerId, Throwable e) {
		log.info("Error in fetching customer details from customer API {}", customerId);
		util.parseExceptionAndThrowIfExists(e);
		throw new PaymentServiceException(prop.getCustomerApiNotAvailable());
	}
}
