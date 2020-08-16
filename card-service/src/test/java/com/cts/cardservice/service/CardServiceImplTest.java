package com.cts.cardservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.cts.cardservice.config.CardPropConfig;
import com.cts.cardservice.dao.Card;
import com.cts.cardservice.dao.CardDaoService;
import com.cts.cardservice.dao.Customer;
import com.cts.cardservice.exception.CardServiceException;
import com.cts.cardservice.model.CardRequest;

class CardServiceImplTest {

	@Mock
	private CardDaoService dao;
	@Mock
	private CustomerServiceClient client;
	@Mock
	private CardPropConfig prop;

	private CardService service;

	@BeforeEach
	void setUp() throws Exception {
		client = Mockito.mock(CustomerServiceClient.class);
		dao = Mockito.mock(CardDaoService.class);
		prop = Mockito.mock(CardPropConfig.class);
		service = new CardServiceImpl(dao, prop, client);
	}

	@Test
	void testGetAllCardsUsingCustomersAPI_Empty() {
		List<Card> cards = service.getAllCardsUsingCustomersAPI();
		assertThat(cards).isEmpty();
	}
	
	@Test
	void testGetAllCardsUsingCustomersAPI() {
		when(client.getCustomers()).thenReturn(Arrays.asList(Customer.builder().build()));
		List<Card> cards = service.getAllCardsUsingCustomersAPI();
		assertThat(cards).isEmpty();
	}

	@Test
	void testGetAllCardsUsingCustomerAPI_empty() {
		when(client.getCustomer(1)).thenReturn(Customer.builder().id(1).build());
		List<Card> cards = service.getAllCardsUsingCustomerAPI(1);
		assertThat(cards).isEmpty();
	}
	
	@Test
	void testGetAllCardsUsingCustomerAPI() {
		when(client.getCustomer(1)).thenReturn(Customer.builder().id(1).build());
		when(dao.getCustomerCards(1)).thenReturn(Arrays.asList(Card.builder().build()));
		List<Card> cards = service.getAllCardsUsingCustomerAPI(1);
		assertThat(cards).isNotEmpty();
	}
	
	@Test
	void testGetAllCardsUsingCustomerAPI_null() {
		when(client.getCustomer(1)).thenReturn(Customer.builder().id(1).build());
		when(dao.getCustomerCards(1)).thenReturn(null);
		List<Card> cards = service.getAllCardsUsingCustomerAPI(1);
		assertThat(cards).isEmpty();
	}

	@Test
	void testGetCardOfCustomer_exception() {
		when(client.getCustomer(1)).thenReturn(Customer.builder().id(1).build());
		Assertions.assertThrows(CardServiceException.class, () -> {
			service.getCardOfCustomer(1, 1L);
		});
	}
	
	@Test
	void testGetCardOfCustomer() {
		when(client.getCustomer(1)).thenReturn(Customer.builder().id(1).build());
		when(dao.getCustomerCards(1)).thenReturn(Arrays.asList(Card.builder().cardNumber(1L).build()));
		Card card = service.getCardOfCustomer(1, 1L);
		assertThat(card).isNotNull();
	}

	@Test
	void testSaveOrUpdate() {
		when(client.getCustomer(1)).thenReturn(Customer.builder().id(1).build());
		CardRequest request = new CardRequest();
		request.setCardType(CardRequest.CardType.VISA);
		CardRequest.CardType.VISA.getValue();
		request.setExpirationDate("12/12/2012");
		assertThat(service.saveOrUpdate(request, 1, 1L)).isTrue();
	}

}
