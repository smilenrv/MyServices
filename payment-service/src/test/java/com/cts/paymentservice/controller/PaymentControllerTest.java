package com.cts.paymentservice.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.HashMap;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.cts.paymentservice.config.PaymentPropConfig;
import com.cts.paymentservice.exception.PaymentServiceExceptionHandler;
import com.cts.paymentservice.service.PaymentService;
import com.cts.paymentservice.validator.DateValidator;

@SpringBootTest
@TestPropertySource("classpath:application-test.properties")
class PaymentControllerTest {

	private MockMvc mockMvc;

	@InjectMocks
	private PaymentController controller;

	@Mock
	private PaymentService service;
	
	@Value("${card.dateformat}")
	private String dateFormat;
	
	@BeforeEach
	void setUp() throws Exception {
		DateValidator df = new DateValidator();
		ReflectionTestUtils.setField(df, "dateFormat", dateFormat);
		PaymentPropConfig prop = new PaymentPropConfig();
		HashMap<String, String> errMessage = new HashMap<>();
		prop.setErrMessage(errMessage);
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new PaymentServiceExceptionHandler(prop))
				.build();
		System.out.println("------------->"+dateFormat);
	}

	@Test
	void testTrackPayment() throws Exception {
		mockMvc.perform(post("/web/payment/service/1/payment").contentType(MediaType.APPLICATION_JSON)
				.content("{\"cardNumber\":1234567890123457,\"cardType\":\"VISA\",\"expirationDate\":\"11/30/2011\", \"status\": \"OPEN\", \"amount\": 234}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
	}

	@Test
	void testGetPayments() throws Exception {
		mockMvc.perform(get("/web/payment/service/1/payments").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

}
