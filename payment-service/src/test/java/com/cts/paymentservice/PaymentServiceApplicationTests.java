package com.cts.paymentservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.paymentservice.config.PaymentPropConfig;

@SpringBootTest
class PaymentServiceApplicationTests {

	@Test
	public void applicationContextLoaded() {
		PaymentPropConfig prop = new PaymentPropConfig();
		assertThat(prop).isNotNull();
	}

	@Test
	public void applicationContextTest() {
		PaymentServiceApplication.main(new String[] {});
		PaymentPropConfig prop = new PaymentPropConfig();
		assertThat(prop).isNotNull();
	}

}
