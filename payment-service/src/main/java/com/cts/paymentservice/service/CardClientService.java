package com.cts.paymentservice.service;

import org.springframework.cloud.netflix.ribbon.RibbonClient;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.cts.paymentservice.config.PaymentPropConfig;
import com.cts.paymentservice.dao.Card;
import com.cts.paymentservice.exception.PaymentServiceException;
import com.cts.paymentservice.model.ApiResponse;
import com.cts.paymentservice.util.UtilityService;
import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RibbonClient(name="card-service")
public class CardClientService {

	private RestTemplate restTemplate;
	private PaymentPropConfig prop;
	private UtilityService util;

	public CardClientService(PaymentPropConfig prop, UtilityService util) {
		this.restTemplate = new RestTemplate();
		this.prop = prop;
		this.util = util;
	}

	// Validate customer & card exists
	// perform input card belongs to customer
	@HystrixCommand(fallbackMethod = "getCardFallBack", commandKey = "card-api")
	public Card getCardOfCustomerUsingCardAPI(Integer customerId, Long cardNumber) {
		String service = util.getServiceUrl("card-service");
		log.info("fetch Card" + service);
		ApiResponse<Card> response = restTemplate.exchange(service + prop.getFetchCardOfCustomer(), HttpMethod.GET,
				null, new ParameterizedTypeReference<ApiResponse<Card>>() {
				}, customerId, cardNumber).getBody();
		log.info("Get customer API Response {}", response);
		return (Card) response.getData();
	}

	public Card getCardFallBack(Integer customerId, Long cardNumber, Throwable e) {
		log.info("Error in fetching card details from card API" + e);
		util.parseExceptionAndThrowIfExists(e);
		throw new PaymentServiceException(prop.getCardApiNotAvailable());
	}
}
