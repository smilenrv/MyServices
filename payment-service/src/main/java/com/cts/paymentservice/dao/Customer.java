package com.cts.paymentservice.dao;

import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Customer {
	@Id
	@GeneratedValue
	private Integer id;
	private String name;
	private int age;
	private String emailAddress;
	private String address;
	private String phoneNumber;

	@OneToMany(mappedBy = "customer")
	@JsonIgnore
	private List<Card> cards;

	@OneToMany(mappedBy = "customer")
	@JsonIgnore
	private List<Payment> payments;

}
