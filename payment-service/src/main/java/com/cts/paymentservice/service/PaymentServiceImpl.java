package com.cts.paymentservice.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.cts.paymentservice.config.PaymentPropConfig;
import com.cts.paymentservice.dao.Card;
import com.cts.paymentservice.dao.Customer;
import com.cts.paymentservice.dao.Payment;
import com.cts.paymentservice.dao.PaymentDaoService;
import com.cts.paymentservice.exception.PaymentServiceException;
import com.cts.paymentservice.model.PaymentRequest;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class PaymentServiceImpl implements PaymentService {

	private PaymentDaoService dao;
	private PaymentPropConfig prop;
	private CardClientService cardService;
	private CustomerClientService customerService;

	public PaymentServiceImpl(PaymentDaoService dao, PaymentPropConfig prop, CardClientService cardService,
			CustomerClientService customerService) {
		this.dao = dao;
		this.prop = prop;
		this.cardService = cardService;
		this.customerService = customerService;
	}

	private static final String OPEN = "Open";
	private static final String CLOSED = "Closed";
	private static final String FAILURE = "Failure";

	public boolean initiatePaymentTransaction(Integer customerId, PaymentRequest request) {
		//Validate card & customer valid and available
		Card card = cardService.getCardOfCustomerUsingCardAPI(customerId, Long.valueOf(request.getCardNumber()));
		if (null == card) {
			throw new PaymentServiceException(prop.getCardNotAvailable());
		}
		//fetch the customer details
		Customer customer = customerService.getCustomerUsingCustomerAPI(customerId);
		card.setCustomer(customer);
		Card cardRequest = Card.builder().cardNumber(Long.valueOf(request.getCardNumber()))
				.cardType(request.getCardType().getValue()).customer(customer)
				.expirationDate(request.getExpirationDate()).id(card.getId()).build();
		log.info("Comparison begins");
		if (!compareCardObject(card, cardRequest)) {
			throw new PaymentServiceException(prop.getCardNotValid());
		}
		log.info("Comparison ends");
		/**
		 * Below not required as h2 made as application wise
		 */
		/** dao.doCustomerCardPersistInPaymentSession(card); */

		switch (request.getStatus().getValue()) {
		case OPEN:
			return performOpenTransaction(request, card.getCustomer());
		case CLOSED:
			return performCloseFailureTransaction(request, card.getCustomer());
		case FAILURE:
			return performCloseFailureTransaction(request, card.getCustomer());
		default:
			throw new PaymentServiceException(prop.getPaymentInvalid());
		}
	}

	private boolean performOpenTransaction(PaymentRequest request, Customer customer) {
		// should not have open transaction
		if (dao.noOpenTransactionExists(customer)) {
			return dao.savePayment(Payment.builder().amount(request.getAmount()).customer(customer)
					.status(request.getStatus().getValue()).cardNumber(Long.valueOf(request.getCardNumber()))
					.cardType(request.getCardType().getValue()).expirationDate(request.getExpirationDate()).build());
		}
		throw new PaymentServiceException(prop.getPaymentFailOnOpen());
	}

	private boolean performCloseFailureTransaction(PaymentRequest request, Customer customer) {
		Payment payment = dao.getOpenTransactionPayment(customer.getId());
		if (null == payment) {
			throw new PaymentServiceException(prop.getPaymentFailOnOtherStatus());
		}
		payment.setAmount(request.getAmount());
		payment.setStatus(request.getStatus().getValue());
		payment.setCustomer(customer);
		dao.savePayment(payment);
		return true;
	}

	// perform input card detail is same of stored card detail
	private boolean compareCardObject(Card dbObject, Card inputObject) {
		return dbObject.equals(inputObject);
	}

	public List<Payment> getPayments(Integer customerId) {
		Customer customer = customerService.getCustomerUsingCustomerAPI(customerId);
		return dao.getCustomerPayment(customer.getId());
	}
}
