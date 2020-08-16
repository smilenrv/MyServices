package com.cts.paymentservice.dao;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

class PaymentDaoServiceTest {

	@Mock
	private CardRepository cardRepo;
	@Mock
	private CustomerRepository customerRepo;
	@Mock
	private PaymentRepository paymentRepo;

	private PaymentDaoService dao;

	@BeforeEach
	void setUp() throws Exception {
		cardRepo = Mockito.mock(CardRepository.class);
		customerRepo = Mockito.mock(CustomerRepository.class);
		paymentRepo = Mockito.mock(PaymentRepository.class);
		dao = new PaymentDaoService(cardRepo, customerRepo, paymentRepo);
	}

	@Test
	void testGetAllPayments() {
		when(paymentRepo.findAll()).thenReturn(Arrays.asList(Payment.builder().build()));
		List<Payment> paymentList = dao.getAllPayments();
		assertThat(paymentList).isNotEmpty();
	}

	@Test
	void testNoOpenTransactionExists() {
		when(paymentRepo.findByCustomerId(Mockito.anyInt())).thenReturn(Arrays.asList(Payment.builder().status("Closed").build()));
		boolean value = dao.noOpenTransactionExists(Customer.builder().id(1).build());
		assertThat(value).isTrue();
	}
	
	@Test
	void testNoOpenTransactionExists_nopayment() {
		when(paymentRepo.findByCustomerId(Mockito.anyInt())).thenReturn(Collections.emptyList());
		boolean value = dao.noOpenTransactionExists(Customer.builder().id(1).build());
		assertThat(value).isTrue();
	}
	
	@Test
	void testNoOpenTransactionExists_nopayment1() {
		when(paymentRepo.findByCustomerId(Mockito.anyInt())).thenReturn(null);
		boolean value = dao.noOpenTransactionExists(Customer.builder().id(1).build());
		assertThat(value).isTrue();
	}
	
	@Test
	void testNoOpenTransactionExists_false() {
		when(paymentRepo.findByCustomerId(Mockito.anyInt())).thenReturn(Arrays.asList(Payment.builder().status("Open").build()));
		boolean value = dao.noOpenTransactionExists(Customer.builder().id(1).build());
		assertThat(value).isFalse();
	}

	@Test
	void testGetCustomerPayment() {
		when(paymentRepo.findByCustomerId(Mockito.anyInt())).thenReturn(Arrays.asList(Payment.builder().build()));
		List<Payment> paymentList = dao.getCustomerPayment(1);
		assertThat(paymentList).isNotEmpty();
	}

	@Test
	void testGetCustomer_null() {
		when(customerRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Customer customer = dao.getCustomer(1);
		assertThat(customer).isNull();
	}
	
	@Test
	void testGetCustomer() {
		when(customerRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(Customer.builder().build()));
		Customer customer = dao.getCustomer(1);
		assertThat(customer).isNotNull();
	}

	@Test
	void testGetCard_null() {
		when(cardRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		Card card = dao.getCard(1);
		assertThat(card).isNull();
	}
	
	@Test
	void testGetCard() {
		when(cardRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(Card.builder().build()));
		Card card = dao.getCard(1);
		assertThat(card).isNotNull();
	}

	@Test
	void testSavePayment() {
		Payment.builder().customer(Customer.builder().id(1).build()).build().getCustomerValue();
		Card.builder().customer(Customer.builder().id(1).build()).build().getCustomerId();
		when(customerRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		when(cardRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(Card.builder().build()));
		dao.doCustomerCardPersistInPaymentSession(Card.builder().id(1).customer(Customer.builder().id(1).build()).build());
		assertThat(dao.savePayment(Payment.builder().build())).isTrue();
	}
	
	@Test
	void testSavePayment1() {
		when(customerRepo.findById(Mockito.anyInt())).thenReturn(Optional.of(Customer.builder().build()));
		when(cardRepo.findById(Mockito.anyInt())).thenReturn(Optional.empty());
		dao.doCustomerCardPersistInPaymentSession(Card.builder().id(1).customer(Customer.builder().id(1).build()).build());
		assertThat(dao.savePayment(Payment.builder().build())).isTrue();
	}

	@Test
	void testGetOpenTransactionPayment() {
		when(paymentRepo.findByCustomerId(Mockito.anyInt())).thenReturn(Arrays.asList(Payment.builder().status("Open").build()));
		Payment value = dao.getOpenTransactionPayment(1);
		assertThat(value).isNotNull();
	}
	
	@Test
	void testGetOpenTransactionPayment_null() {
		when(paymentRepo.findByCustomerId(Mockito.anyInt())).thenReturn(Arrays.asList(Payment.builder().status("Closed").build()));
		Payment value = dao.getOpenTransactionPayment(1);
		assertThat(value).isNull();
	}

}
