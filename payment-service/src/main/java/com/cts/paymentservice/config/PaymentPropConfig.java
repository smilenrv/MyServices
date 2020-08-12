package com.cts.paymentservice.config;

import java.util.HashMap;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@EnableConfigurationProperties
@ConfigurationProperties(prefix = "config.prop")
@Data
public class PaymentPropConfig {

	private HashMap<String, String> errMessage;

	private String fetchCardOfCustomer;
	private String fetchCustomer;
	
	private String customerNotAvailable;
	private String cardNotAvailable;
	private String cardNotValid;
	private String paymentInvalid;
	
	private String paymentFailOnOtherStatus;
	private String paymentFailOnOpen;
	
	private String cardApiNotAvailable;
	private String customerApiNotAvailable;

}
