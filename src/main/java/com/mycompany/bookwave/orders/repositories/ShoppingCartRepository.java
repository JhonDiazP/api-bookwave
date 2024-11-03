package com.mycompany.bookwave.orders.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mycompany.bookwave.orders.entities.ShoppingCarts;

public interface ShoppingCartRepository extends JpaRepository<ShoppingCarts, Integer> {
    
    @Query("""
            SELECT sc.status.id
            FROM ShoppingCarts sc
            WHERE sc.user.id = :userId AND sc.status.id != 1
            """)
    Integer getStatusByUserId(String userId);

    @Query("""
            SELECT sc.id
            FROM ShoppingCarts sc
            WHERE sc.user.id = :userId AND sc.status.id = 1
            """)
    Integer getByUserId(String userId);
}
