package com.mycompany.bookwave.orders.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.mycompany.bookwave.catalog.entities.Book;
import com.mycompany.bookwave.orders.entities.ShoppingBook;

public interface ShoppingBooksRepository extends JpaRepository<ShoppingBook, Integer> {

    @Query("SELECT sb.book FROM ShoppingBook sb WHERE sb.shoppingCarts.id = ?1")
    List<Book> findByShoppingCartsId(Integer shoppingCartsId);

    @Query("SELECT sb FROM ShoppingBook sb WHERE sb.shoppingCarts.id = ?1")
    List<ShoppingBook> findShoppingBooksByShoppingCartId(Integer shoppingCartId);
}
