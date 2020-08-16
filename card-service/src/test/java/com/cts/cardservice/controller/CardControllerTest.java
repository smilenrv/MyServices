package com.cts.cardservice.controller;

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
import org.springframework.web.client.HttpClientErrorException;

import com.cts.cardservice.config.CardPropConfig;
import com.cts.cardservice.exception.CardExceptionHandler;
import com.cts.cardservice.exception.CardServiceException;
import com.cts.cardservice.service.CardService;

class CardControllerTest {
	
	private MockMvc mockMvc;

	@InjectMocks
	private CardController controller;

	@Mock
	private CardService service;

	@BeforeEach
	void setUp() throws Exception {
		CardPropConfig prop = new CardPropConfig();
		HashMap<String, String> errMessage = new HashMap<>();
		errMessage.put("phone.notvalid", "Phone is not valid");
		prop.setErrMessage(errMessage);
				
		MockitoAnnotations.initMocks(this);
		mockMvc = MockMvcBuilders.standaloneSetup(controller).setControllerAdvice(new CardExceptionHandler(prop))
				.build();
	}

	@Test
	void testGetCards() throws Exception {
		mockMvc.perform(get("/web/card/service/cards").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	void testGetCardsOfCustomer() throws Exception {
		mockMvc.perform(get("/web/card/service/1/cards").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}

	@Test
	void testGetCardOfCustomer() throws Exception {
		mockMvc.perform(get("/web/card/service/1/cards/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isOk());
	}
	
	@Test
	void testGetCardOfCustomer_400() throws Exception {
		when(service.getCardOfCustomer(Mockito.anyInt(), Mockito.anyLong())).thenThrow(HttpClientErrorException.class);
		mockMvc.perform(get("/web/card/service/1/cards/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isBadRequest());
	}
	
	@Test
	void testGetCardOfCustomer_500() throws Exception {
		when(service.getCardOfCustomer(Mockito.anyInt(), Mockito.anyLong())).thenThrow(NullPointerException.class);
		mockMvc.perform(get("/web/card/service/1/cards/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isInternalServerError());
	}
	
	
	@Test
	void testGetCardOfCustomer_422() throws Exception {
		when(service.getCardOfCustomer(Mockito.anyInt(), Mockito.anyLong())).thenThrow(CardServiceException.class);
		mockMvc.perform(get("/web/card/service/1/cards/1").contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isExpectationFailed());
	}
	

	@Test
	void testCreateOrUpdateCard()  throws Exception {
		mockMvc.perform(post("/web/card/service/1/card/1234567890123456").contentType(MediaType.APPLICATION_JSON)
				.content("{\"cardType\":\"VISA\",\"expirationDate\":\"11/30/2011\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
	}
	
	@Test
	void testCreateOrUpdateCard_error() throws Exception {
		mockMvc.perform(post("/web/card/service/1/card/1").contentType(MediaType.APPLICATION_JSON)
				.content("{\"cardType\":\"VISA\",\"expirationDate\":\"11/30/2012sadg\"}")
				.accept(MediaType.APPLICATION_JSON)).andExpect(status().isUnprocessableEntity());
	}

}
