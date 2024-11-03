package com.mycompany.bookwave.payments.entities;

import com.mycompany.bookwave.orders.entities.ShoppingBook;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "pays_shopping_books")
public class Buy {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "pay_id", nullable = false)
    private Pay pay;
    
    @ManyToOne
    @JoinColumn(name = "shopping_book_id", nullable = false)
    private ShoppingBook shoppingBook;
}
