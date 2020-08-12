package com.cts.paymentservice.model;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CustomerReponse {
	private Integer id;
	private String name;
	private int age;
	private String emailAddress;
	private String address;
	private String phoneNumber;
}
