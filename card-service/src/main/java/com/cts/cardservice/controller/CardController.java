package com.cts.cardservice.controller;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.Pattern;

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

import com.cts.cardservice.dao.Card;
import com.cts.cardservice.model.ApiResponse;
import com.cts.cardservice.model.CardRequest;
import com.cts.cardservice.service.CardService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/web/card/service/")
@Slf4j
@Validated
public class CardController {

	private CardService service;

	public CardController(CardService service) {
		this.service = service;
	}

	@ApiOperation(value = "Get all cards", notes = "To fetch all Cards of any customers", response = ApiResponse.class, produces = "application/json")
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "Ok, Cards is fetched from DB"),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error"),
			@io.swagger.annotations.ApiResponse(code = 417, message = "Expectation failed"),
			@io.swagger.annotations.ApiResponse(code = 422, message = "Unprocessable entity"), })
	@GetMapping(path = "/cards", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCards() {
		List<Card> customers = service.getAllCardsUsingCustomersAPI();
		return buildResponse(customers);
	}

	@ApiOperation(value = "Get all cards of specific Customer", notes = "To fetch all Cards of specific custorm", response = ApiResponse.class, produces = "application/json")
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "Ok, Cards is fetched from DB"),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error"),
			@io.swagger.annotations.ApiResponse(code = 417, message = "Expectation failed"),
			@io.swagger.annotations.ApiResponse(code = 422, message = "Unprocessable entity"), })
	@GetMapping(path = "/{customerId}/cards", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCardsOfCustomer(
			@ApiParam(required = true, type = "Integer") @PathVariable int customerId) {
		List<Card> cards = service.getAllCardsUsingCustomerAPI(customerId);
		log.info(" " + cards);
		return buildResponse(cards);
	}

	@ApiOperation(value = "Get specific sards of specific customer", notes = "To fetch specific Cards of specific customer", response = ApiResponse.class, produces = "application/json")
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "Ok, Cards is fetched from DB"),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error"),
			@io.swagger.annotations.ApiResponse(code = 417, message = "Expectation failed"),
			@io.swagger.annotations.ApiResponse(code = 422, message = "Unprocessable entity"), })
	@GetMapping(path = "/{customerId}/cards/{cardNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> getCardOfCustomer(
			@ApiParam(required = true, type = "Integer") @PathVariable int customerId,
			@ApiParam(required = true, type = "Long") @Pattern(regexp = "^[0-9]{16}$", message = "card.number.notvalid") @PathVariable String cardNumber) {
		Card card = service.getCardOfCustomer(customerId, Long.valueOf(cardNumber));
		log.info(" " + card);
		return buildResponse(card);
	}

	@ApiOperation(value = "Create/Update card for a customer", notes = "To create/update card into DB", response = ApiResponse.class, produces = "application/json")
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "Ok, card detail is created/updated"),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error"),
			@io.swagger.annotations.ApiResponse(code = 417, message = "Expectation failed"),
			@io.swagger.annotations.ApiResponse(code = 422, message = "Unprocessable entity"), })
	@PostMapping(path = "/{customerId}/card/{cardNumber}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<?> createOrUpdateCard(
			@ApiParam(required = true, type = "Integer") @PathVariable Integer customerId,
			@ApiParam(required = true, type = "String") @PathVariable @Pattern(regexp = "^[0-9]{16}$", message = "card.number.notvalid") String cardNumber,
			@Valid @RequestBody CardRequest request) {
		return buildResponse(service.saveOrUpdate(request, customerId, Long.valueOf(cardNumber)));
	}

	private <T> ResponseEntity<?> buildResponse(T t) {
		return ResponseEntity.ok(ApiResponse.builder().code(200).message(HttpStatus.OK.name()).data(t).build());
	}
}
