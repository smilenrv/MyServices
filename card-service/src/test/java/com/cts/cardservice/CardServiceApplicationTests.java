package com.cts.cardservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.cardservice.config.CardPropConfig;

@SpringBootTest
class CardServiceApplicationTests {

	@Test
	public void applicationContextLoaded() {
		CardPropConfig card = new CardPropConfig();
		assertThat(card).isNotNull();
	}

	@Test
	public void applicationContextTest() {
		CardServiceApplication.main(new String[] {});
		CardPropConfig card = new CardPropConfig();
		assertThat(card).isNotNull();
	}

}
