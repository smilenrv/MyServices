package com.cts.paymentservice.dao;

import java.util.List;

import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class PaymentDaoService {

	private CardRepository cardRepo;
	private CustomerRepository customerRepo;
	private PaymentRepository paymentRepo;

	public PaymentDaoService(CardRepository cardRepo, CustomerRepository customerRepo, PaymentRepository paymentRepo) {
		this.cardRepo = cardRepo;
		this.customerRepo = customerRepo;
		this.paymentRepo = paymentRepo;
	}

	public List<Payment> getAllPayments() {
		return paymentRepo.findAll();
	}

	public boolean noOpenTransactionExists(Customer customer) {
		List<Payment> paymentList = getCustomerPayment(customer.getId());
		if (null == paymentList || paymentList.isEmpty()) {
			return true;
		}
		return paymentList.stream().noneMatch(e -> e.getStatus().equalsIgnoreCase("Open"));
	}

	public List<Payment> getCustomerPayment(Integer customerId) {
		return paymentRepo.findByCustomerId(customerId);
		/**
		 * return customerRepo.findById(customerId).map(e ->
		 * e.getPayments()).orElse(Collections.emptyList());
		 */
	}

	public Customer getCustomer(Integer customerId) {
		return customerRepo.findById(customerId).orElse(null);
	}

	public Card getCard(Integer cardId) {
		return cardRepo.findById(cardId).orElse(null);
	}

	public void doCustomerCardPersistInPaymentSession(Card inputCard) {
		Customer customer = inputCard.getCustomer();
		if (null == getCustomer(customer.getId())) {
			log.info("Save customer in payment session as its not available in h2");
			customerRepo.save(customer);
		}
		if (null == getCard(inputCard.getId())) {
			log.info("Save card in payment session as its not available in h2");
			inputCard.setCustomer(customer);
			cardRepo.save(inputCard);
		}
	}

	public boolean savePayment(Payment payment) {
		paymentRepo.save(payment);
		return true;
	}

	public Payment getOpenTransactionPayment(Integer customerId) {
		List<Payment> paymentList = getCustomerPayment(customerId);
		return paymentList.stream().filter(e -> e.getStatus().equalsIgnoreCase("Open")).findFirst().orElse(null);
	}

}
