package com.cts.paymentservice.controller;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.paymentservice.model.ApiResponse;
import com.cts.paymentservice.model.PaymentRequest;
import com.cts.paymentservice.service.PaymentService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/web/payment/service/")
@Slf4j
@Validated
public class PaymentController {

	private PaymentService service;

	public PaymentController(PaymentService service) {
		this.service = service;
	}

	@ApiOperation(value = "Initiate payment status for a customer", notes = "To create/update transaction into DB", response = ApiResponse.class, produces = "application/json")
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "Ok, Payment transaction is created/updated"),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error"),
			@io.swagger.annotations.ApiResponse(code = 417, message = "Expectation failed"),
			@io.swagger.annotations.ApiResponse(code = 422, message = "Unprocessable entity"), })
	@PostMapping(path = "/{customerId}/payment", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> trackPayment(@ApiParam(required = true, type = "Integer") @PathVariable Integer customerId,
			@Valid @RequestBody PaymentRequest request) {
		log.info("Initiate transaction");
		return buildResponse(service.initiatePaymentTransaction(customerId, request));
	}

	@ApiOperation(value = "Get payments for a customer", notes = "Fetch payment list of customer", response = ApiResponse.class, produces = "application/json")
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "Ok, Fetched"),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error"),
			@io.swagger.annotations.ApiResponse(code = 417, message = "Expectation failed"),
			@io.swagger.annotations.ApiResponse(code = 422, message = "Unprocessable entity"), })
	@GetMapping(path = "/{customerId}/payments", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getPayments(
			@ApiParam(required = true, type = "Integer") @PathVariable Integer customerId) {
		log.info("get payment list");
		return buildResponse(service.getPayments(customerId));
	}

	private <T> ResponseEntity<?> buildResponse(T t) {
		return ResponseEntity.ok(ApiResponse.builder().code(200).message(HttpStatus.OK.name()).data(t).build());
	}
}
