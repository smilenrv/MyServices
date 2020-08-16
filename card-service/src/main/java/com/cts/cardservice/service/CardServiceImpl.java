package com.cts.cardservice.service;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.cts.cardservice.config.CardPropConfig;
import com.cts.cardservice.dao.Card;
import com.cts.cardservice.dao.CardDaoService;
import com.cts.cardservice.dao.Customer;
import com.cts.cardservice.exception.CardServiceException;
import com.cts.cardservice.model.CardRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CardServiceImpl implements CardService {

	private CardDaoService dao;
	private CustomerServiceClient service;
	private CardPropConfig prop;

	public CardServiceImpl(CardDaoService dao, CardPropConfig prop, CustomerServiceClient service) {
		this.dao = dao;
		this.service = service;
		this.prop = prop;
	}

	

	public List<Card> getAllCardsUsingCustomersAPI() {
		List<Customer> customerList = service.getCustomers();

		return customerList.stream().filter(Objects::nonNull).map(e -> dao.getCustomerCards(e.getId()))
				.collect(Collectors.toList()).stream().flatMap(List::stream).collect(Collectors.toList());
	}

	public List<Card> getAllCardsUsingCustomerAPI(Integer customerId) {
		Customer customer = service.getCustomer(customerId);

		int id = customer.getId();
		List<Card> cardList = dao.getCustomerCards(id);
		return null == cardList || cardList.isEmpty() ? Collections.emptyList() : cardList;
	}

	public Card getCardOfCustomer(Integer customerId, Long cardNumber) {
		return getAllCardsUsingCustomerAPI(customerId).stream().filter(e -> (cardNumber.equals(e.getCardNumber()))).findFirst()
				.orElseThrow(() -> new CardServiceException(prop.getCardNotAvailable()));
	}

	public boolean saveOrUpdate(CardRequest card, Integer customerId, Long cardId) {
		Customer customer = service.getCustomer(customerId);
		/**
		 * Customer customer = dao.getCustomer(customerAPI.getId()); if (null ==
		 * customer) { log.info("cusotmer not exists in card api");
		 * dao.saveCustomer(customerAPI); }
		 */
		log.info("customer now exists in card api");
		dao.saveOrUpdateCard(
				Card.builder().cardNumber(cardId).cardType(card.getCardType().getValue())
						.expirationDate(card.getExpirationDate()).customer(customer).build());
		return true;
	}
}
