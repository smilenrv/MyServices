package com.cts.cardservice.dao;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface CardRepository extends JpaRepository<Card, Integer> {
		List<Card> findByCustomer_Id(Integer customerId);
}
