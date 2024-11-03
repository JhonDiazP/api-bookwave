package com.mycompany.bookwave.payments.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.bookwave.payments.entities.PayShoppingBook;

public interface PayShoppingBookRepository extends JpaRepository<PayShoppingBook, Integer> {
    
}
