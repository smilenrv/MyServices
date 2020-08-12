package com.cts.cardservice.service;

import java.util.List;

import com.cts.cardservice.dao.Card;
import com.cts.cardservice.model.CardRequest;

public interface CardService {
	Card getCardOfCustomer(Integer customerId, Long cardNumber);

	List<Card> getAllCardsUsingCustomersAPI();

	List<Card> getAllCardsUsingCustomerAPI(Integer customerId);

	boolean saveOrUpdate(CardRequest card, Integer customerId, Long cardId);
}
