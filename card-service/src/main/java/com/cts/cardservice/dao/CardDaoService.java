package com.cts.cardservice.dao;

import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class CardDaoService {
	
	private CardRepository repository;
	private CustomerRepository customerRepo;

	public CardDaoService(CardRepository repository, CustomerRepository customerRepo) {
		this.repository = repository;
		this.customerRepo = customerRepo;
	}

	public List<Card> getAllCards() {
		return repository.findAll();
	}

	public List<Card> getCustomerCards(Integer customerId) {
		//return repository.findByCustomer_Id(customerId);
		return customerRepo.findById(customerId).map(Customer::getCards).orElse(Collections.emptyList());
	}

	public Customer getCustomer(Integer customerId) {
		return customerRepo.findById(customerId).orElse(null);
	}

	public boolean saveOrUpdateCard(Card card) {
		List<Card> cardList = getCustomerCards(card.getCustomerId());
		/**
		 * Customer customer = card.getCustomer(); if(customer != null) { cardList =
		 * customer.getCards(); }
		 */
		if (cardList != null && !cardList.isEmpty()) {
			log.info("Fetch the existing card and set the id into new object");
			Integer id = cardList.stream().filter(e -> card.getCardNumber().equals(e.getCardNumber()))
					.map(Card::getId).findAny().orElse(null);
			card.setId(id);
		}
		repository.save(card);
		return true;
	}

	public void saveCustomer(Customer customer) {
		customerRepo.save(customer);
	}
}
