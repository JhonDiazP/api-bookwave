package com.mycompany.bookwave.payments.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.bookwave.payments.entities.Pay;

public interface PayRepository extends JpaRepository<Pay, String> {
    
}
