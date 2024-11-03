package com.mycompany.bookwave.payments.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.bookwave.payments.entities.Buy;

public interface BuyRepository extends JpaRepository<Buy, Integer> {
    
}
