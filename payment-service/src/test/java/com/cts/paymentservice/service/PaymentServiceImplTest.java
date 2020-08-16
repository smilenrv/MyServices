package com.cts.paymentservice.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.Collections;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import com.cts.paymentservice.config.PaymentPropConfig;
import com.cts.paymentservice.dao.Card;
import com.cts.paymentservice.dao.Customer;
import com.cts.paymentservice.dao.Payment;
import com.cts.paymentservice.dao.PaymentDaoService;
import com.cts.paymentservice.exception.PaymentServiceException;
import com.cts.paymentservice.model.PaymentRequest;

class PaymentServiceImplTest {

	@Mock
	private PaymentDaoService dao;
	@Mock
	private PaymentPropConfig prop;
	@Mock
	private CardClientService cardService;
	@Mock
	private CustomerClientService customerService;

	private PaymentService service;

	@BeforeEach
	void setUp() throws Exception {
		dao = Mockito.mock(PaymentDaoService.class);
		prop = Mockito.mock(PaymentPropConfig.class);
		cardService = Mockito.mock(CardClientService.class);
		customerService = Mockito.mock(CustomerClientService.class);
		service = new PaymentServiceImpl(dao, prop, cardService, customerService);
	}

	@Test
	void testInitiatePaymentTransaction_nullCard() {
		when(cardService.getCardOfCustomerUsingCardAPI(Mockito.anyInt(), Mockito.anyLong())).thenReturn(null);
		PaymentRequest req = new PaymentRequest();
		req.setCardNumber("1234567890123456");
		PaymentRequest.Status.OPEN.getValue();
		Assertions.assertThrows(PaymentServiceException.class, () -> {
			service.initiatePaymentTransaction(1, req);
		});
	}

	@Test
	void testInitiatePaymentTransaction_Card_comparisonError() {
		when(cardService.getCardOfCustomerUsingCardAPI(Mockito.anyInt(), Mockito.anyLong()))
				.thenReturn(Card.builder().build());
		PaymentRequest req = new PaymentRequest();
		req.setCardNumber("1234567890123456");
		req.setCardType(PaymentRequest.CardType.VISA);
		PaymentRequest.CardType.VISA.getValue();
		PaymentRequest.Status.OPEN.getValue();
		Assertions.assertThrows(PaymentServiceException.class, () -> {
			service.initiatePaymentTransaction(1, req);
		});
	}

	@Test
	void testInitiatePaymentTransaction_Card_Open_error() {
		when(cardService.getCardOfCustomerUsingCardAPI(Mockito.anyInt(), Mockito.anyLong()))
				.thenReturn(Card.builder().cardNumber(1234567890123456L).cardType("Visa").build());
		PaymentRequest req = new PaymentRequest();
		req.setCardNumber("1234567890123456");
		req.setCardType(PaymentRequest.CardType.VISA);
		req.setStatus(PaymentRequest.Status.OPEN);
		PaymentRequest.CardType.VISA.getValue();
		PaymentRequest.Status.OPEN.getValue();
		Assertions.assertThrows(PaymentServiceException.class, () -> {
			service.initiatePaymentTransaction(1, req);
		});
	}
	
	@Test
	void testInitiatePaymentTransaction_Card_Open() {
		when(cardService.getCardOfCustomerUsingCardAPI(Mockito.anyInt(), Mockito.anyLong()))
				.thenReturn(Card.builder().cardNumber(1234567890123456L).cardType("Visa").build());
		PaymentRequest req = new PaymentRequest();
		req.setCardNumber("1234567890123456");
		req.setCardType(PaymentRequest.CardType.VISA);
		req.setStatus(PaymentRequest.Status.OPEN);
		PaymentRequest.CardType.VISA.getValue();
		PaymentRequest.Status.OPEN.getValue();
		when(dao.noOpenTransactionExists(Mockito.any())).thenReturn(true);
		assertThat(service.initiatePaymentTransaction(1, req)).isFalse();
	}
	
	@Test
	void testInitiatePaymentTransaction_Card_Closed_error() {
		when(cardService.getCardOfCustomerUsingCardAPI(Mockito.anyInt(), Mockito.anyLong()))
				.thenReturn(Card.builder().cardNumber(1234567890123456L).cardType("Visa").customer(Customer.builder().id(1).build()).build());
		when(customerService.getCustomerUsingCustomerAPI(Mockito.anyInt())).thenReturn(Customer.builder().id(1).build());
		PaymentRequest req = new PaymentRequest();
		req.setCardNumber("1234567890123456");
		req.setCardType(PaymentRequest.CardType.VISA);
		req.setStatus(PaymentRequest.Status.CLOSED);
		PaymentRequest.CardType.VISA.getValue();
		PaymentRequest.Status.CLOSED.getValue();
		when(dao.getOpenTransactionPayment(Mockito.anyInt())).thenReturn(null);
		Assertions.assertThrows(PaymentServiceException.class, () -> {
			service.initiatePaymentTransaction(1, req);
		});
	}
	
	@Test
	void testInitiatePaymentTransaction_Card_FAILURE() {
		when(cardService.getCardOfCustomerUsingCardAPI(Mockito.anyInt(), Mockito.anyLong()))
				.thenReturn(Card.builder().cardNumber(1234567890123456L).cardType("Visa").build());
		when(customerService.getCustomerUsingCustomerAPI(Mockito.anyInt())).thenReturn(Customer.builder().id(1).build());
		PaymentRequest req = new PaymentRequest();
		req.setCardNumber("1234567890123456");
		req.setCardType(PaymentRequest.CardType.VISA);
		req.setStatus(PaymentRequest.Status.FAILURE);
		PaymentRequest.CardType.VISA.getValue();
		PaymentRequest.Status.FAILURE.getValue();
		when(dao.getOpenTransactionPayment(Mockito.anyInt())).thenReturn(Payment.builder().build());
		assertThat(service.initiatePaymentTransaction(1, req)).isNotNull();
	}
	
	@Test
	void testInitiatePaymentTransaction_Card_Closed() {
		when(cardService.getCardOfCustomerUsingCardAPI(Mockito.anyInt(), Mockito.anyLong()))
				.thenReturn(Card.builder().cardNumber(1234567890123456L).cardType("Visa").build());
		when(customerService.getCustomerUsingCustomerAPI(Mockito.anyInt())).thenReturn(Customer.builder().id(1).build());
		PaymentRequest req = new PaymentRequest();
		req.setCardNumber("1234567890123456");
		req.setCardType(PaymentRequest.CardType.VISA);
		req.setStatus(PaymentRequest.Status.CLOSED);
		PaymentRequest.CardType.VISA.getValue();
		PaymentRequest.Status.FAILURE.getValue();
		when(dao.getOpenTransactionPayment(Mockito.anyInt())).thenReturn(Payment.builder().build());
		assertThat(service.initiatePaymentTransaction(1, req)).isNotNull();
	}

	@Test
	void testGetPayments() {
		when(customerService.getCustomerUsingCustomerAPI(Mockito.anyInt()))
				.thenReturn(Customer.builder().id(1).build());
		when(dao.getCustomerPayment(Mockito.anyInt())).thenReturn(Arrays.asList(Payment.builder().build()));
		assertThat(service.getPayments(1)).isNotEmpty();
	}

	@Test
	void testGetPayments_null() {
		when(customerService.getCustomerUsingCustomerAPI(Mockito.anyInt()))
				.thenReturn(Customer.builder().id(1).build());
		when(dao.getCustomerPayment(Mockito.anyInt())).thenReturn(Collections.emptyList());
		assertThat(service.getPayments(1)).isEmpty();
	}

}
