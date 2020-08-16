package com.cts.customerservice.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cts.customerservice.dao.Customer;
import com.cts.customerservice.model.ApiResponse;
import com.cts.customerservice.model.CustomerRequest;
import com.cts.customerservice.service.CustomerService;

import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@RestController
@RequestMapping("/web/customer/service/")
public class CustomerController {

	private CustomerService service;

	public CustomerController(CustomerService service) {
		this.service = service;
	}

	@ApiOperation(value = "Get All Customers", notes = "To fetch all customers", response = ApiResponse.class, produces = "application/json")
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "Ok, Customer is fetched from DB"),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error"),
			@io.swagger.annotations.ApiResponse(code = 417, message = "Expectation failed"),
			@io.swagger.annotations.ApiResponse(code = 422, message = "Unprocessable entity"), })
	@GetMapping(path = "/customers", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCustomers() {
		List<Customer> customers = service.getAllCustomers();
		return buildResponse(customers);
	}

	@ApiOperation(value = "Get Customer/To validate customer exists", notes = "Used to validate customer exists & can fetch the customer detail", response = ApiResponse.class, produces = "application/json")
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "Ok, Customer is fetched from DB"),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error"),
			@io.swagger.annotations.ApiResponse(code = 417, message = "Expectation failed"),
			@io.swagger.annotations.ApiResponse(code = 422, message = "Unprocessable entity"), })
	@GetMapping(path = "/customers/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> getCustomerDetail(@ApiParam(required = true, type = "Integer") @PathVariable int id) {
		Customer customer = service.getCustomer(id);
		return buildResponse(customer);
	}

	@ApiOperation(value = "Create/Insert customer", notes = "To create/insert customer into DB", response = Boolean.class, produces = "application/json")
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "Ok, customer is created."),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error"),
			@io.swagger.annotations.ApiResponse(code = 417, message = "Expectation failed"),
			@io.swagger.annotations.ApiResponse(code = 422, message = "Unprocessable entity"), })
	@PostMapping(path = "/customer", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> createCustomer(
			@ApiParam(required = true, type = "json", value = "Customer json - Name, Age, Address, Phone, Email Address") @Valid @RequestBody CustomerRequest request) {
		return buildResponse(service.createOrUpdateCustomer(request, null));
	}

	@ApiOperation(value = "Update customer", notes = "To update customer into DB", response = ApiResponse.class, produces = "application/json")
	@io.swagger.annotations.ApiResponses(value = {
			@io.swagger.annotations.ApiResponse(code = 200, message = "Ok, Customer detail is updated"),
			@io.swagger.annotations.ApiResponse(code = 500, message = "Internal server error"),
			@io.swagger.annotations.ApiResponse(code = 417, message = "Expectation failed"),
			@io.swagger.annotations.ApiResponse(code = 422, message = "Unprocessable entity"), })
	@PostMapping(path = "/customer/{id}", produces = MediaType.APPLICATION_JSON_VALUE)
	public ResponseEntity<Object> updateCustomer(@ApiParam(required = true, type = "Integer") @PathVariable Integer id,
			@Valid @RequestBody CustomerRequest request) {
		return buildResponse(service.createOrUpdateCustomer(request, id));
	}

	private <T> ResponseEntity<Object> buildResponse(T t) {
		return ResponseEntity.ok(ApiResponse.builder().code(200).message(HttpStatus.OK.name()).data(t).build());
	}
}
