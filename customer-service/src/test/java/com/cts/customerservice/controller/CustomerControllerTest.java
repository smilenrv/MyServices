package com.cts.customerservice.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cts.customerservice.config.CustomerConfig;
import com.cts.customerservice.exception.CustomerExceptionHandler;
import com.cts.customerservice.exception.CustomerNotFoundException;
import com.cts.customerservice.service.CustomerService;

public class CustomerControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private CustomerController controller;

	@Mock
	private CustomerService service;

	@BeforeEach
	void setUp() throws Exception {
		CustomerConfig prop = new CustomerConfig();
		HashMap<String, String> errMessage = new HashMap<>();
		errMessage.put("phone.notvalid", "Phone is not valid");
		prop.setErrMessage(errMessage);
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new CustomerExceptionHandler(prop))
				.build();
	}

	@Test
	public void test_allcustomer_api() throws Exception {
		mockMvc.perform(get("/web/customer/service/customers").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void test_customer_api() throws Exception {
		mockMvc.perform(get("/web/customer/service/customers/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void test_create_customer_api() throws Exception {
		mockMvc.perform(post("/web/customer/service/customer").contentType(MediaType.APPLICATION_JSON).content(
				"{\"name\":\"Prakash\",\"age\":30,\"emailAddress\":\"test@gmail.com\",\"address\":\"Chennai\",\"phoneNumber\":\"1234567890\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	public void test_update_customer_api() throws Exception {
		mockMvc.perform(post("/web/customer/service/customer/1").contentType(MediaType.APPLICATION_JSON).content(
				"{\"name\":\"Prakash\",\"age\":30,\"emailAddress\":\"test@gmail.com\",\"address\":\"Chennai\",\"phoneNumber\":\"1234567890\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	
	@Test
	public void test_create_customer_api_error_phone() throws Exception {
		mockMvc.perform(post("/web/customer/service/customer").contentType(MediaType.APPLICATION_JSON).content(
				"{\"name\":\"Prakash\",\"age\":30,\"emailAddress\":\"test@gmail.com\",\"address\":\"Chennai\",\"phoneNumber\":\"12345678901234\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void test_create_customer_api_error_email() throws Exception {
		mockMvc.perform(post("/web/customer/service/customer").contentType(MediaType.APPLICATION_JSON).content(
				"{\"name\":\"Prakash\",\"age\":30,\"emailAddress\":\"testgmail.com\",\"address\":\"Chennai\",\"phoneNumber\":\"12345678901234\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	public void test_customer_api_error() throws Exception {
		when(service.getCustomer(Mockito.anyInt())).thenThrow(CustomerNotFoundException.class);
		mockMvc.perform(get("/web/customer/service/customers/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isExpectationFailed());
	}
	
	@Test
	public void test_customer_api_error_500() throws Exception {
		when(service.getCustomer(Mockito.anyInt())).thenThrow(NullPointerException.class);
		mockMvc.perform(get("/web/customer/service/customers/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError());
	}

}
