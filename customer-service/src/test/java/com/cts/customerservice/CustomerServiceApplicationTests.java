package com.cts.customerservice;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import com.cts.customerservice.config.CustomerConfig;

@SpringBootTest
class CustomerServiceApplicationTests {

	@Test
	public void applicationContextLoaded() {
		CustomerConfig config = new CustomerConfig();
		assertThat(config).isNotNull();
	}

	@Test
	public void applicationContextTest() {
		CustomerServiceApplication.main(new String[] {});
		CustomerConfig config = new CustomerConfig();
		assertThat(config).isNotNull();
	}

}
