package com.cts.cardservice.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

class CardDaoServiceTest {

	private CardDaoService dao;

	@Mock
	private CardRepository repository;
	@Mock
	private CustomerRepository customerRepo;

	@BeforeEach
	void setUp() throws Exception {
		repository = Mockito.mock(CardRepository.class);
		customerRepo = Mockito.mock(CustomerRepository.class);
		dao = new CardDaoService(repository, customerRepo);
	}

	@Test
	void test_Fetch_allCards() {
		when(repository.findAll()).thenReturn(
				Arrays.asList(Card.builder().cardNumber(1234L).build(), Card.builder().cardNumber(123456L).build()));
		List<Card> cardList = dao.getAllCards();
		assertThat(cardList).isNotEmpty();
		assertThat(cardList.size()).isEqualTo(2);
		verify(repository).findAll();
	}

	@Test
	void test_fetch_customer_Cards() {
		when(customerRepo.findById(Mockito.anyInt())).thenReturn(java.util.Optional.of(Customer.builder().cards(
				Arrays.asList(Card.builder().cardNumber(1234L).build(), Card.builder().cardNumber(123456L).build())).build()));
		List<Card> cardList = dao.getCustomerCards(1);
		assertThat(cardList).isNotEmpty();
		assertThat(cardList.size()).isEqualTo(2);
	}

	@Test
	void test_fetch_specific_Customer() {
		when(customerRepo.findById(Mockito.anyInt()))
				.thenReturn(java.util.Optional.of(Customer.builder().name("TEST").id(1).build()));
		Customer customer = dao.getCustomer(1);
		dao.saveCustomer(customer);
		assertThat(customer).isNotNull();
		assertThat(customer.getName()).isEqualTo("TEST");
	}

	@Test
	void test_save_update_card() {
		when(customerRepo.findById(Mockito.anyInt())).thenReturn(java.util.Optional.of(Customer.builder().id(1).cards(
				Arrays.asList(Card.builder().cardNumber(1234L).build(), Card.builder().cardNumber(123456L).build())).build()));
		boolean saved = dao
				.saveOrUpdateCard(Card.builder().cardNumber(123L).customer(Customer.builder().id(1).build()).build());
		assertThat(saved).isTrue();
	}
	
	@Test
	void test_save_update_card_empty() {
		when(customerRepo.findById(Mockito.anyInt())).thenReturn(java.util.Optional.of(Customer.builder().id(1).cards(Collections.emptyList()).build()));
		boolean saved = dao
				.saveOrUpdateCard(Card.builder().cardNumber(123L).customer(Customer.builder().id(1).build()).build());
		assertThat(saved).isTrue();
	}
	
	@Test
	void test_save_update_card_null() {
		when(customerRepo.findById(Mockito.anyInt())).thenReturn(java.util.Optional.of(Customer.builder().id(1).cards(null).build()));
		boolean saved = dao
				.saveOrUpdateCard(Card.builder().cardNumber(123L).customer(Customer.builder().id(1).build()).build());
		assertThat(saved).isTrue();
	}
}
