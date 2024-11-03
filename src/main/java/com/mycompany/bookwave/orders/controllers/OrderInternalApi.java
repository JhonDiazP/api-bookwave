package com.mycompany.bookwave.orders.controllers;

import java.util.List;

import com.mycompany.bookwave.catalog.entities.Book;
import com.mycompany.bookwave.orders.entities.ShoppingBook;
import com.mycompany.bookwave.orders.entities.ShoppingCarts;
import com.mycompany.bookwave.users.entities.User;

public interface OrderInternalApi {
    void createShoppingCard(User user);
    List<Book> getBooksInShoppingCard(Integer shoppingCart);
    ShoppingCarts getShoppingCartById(Integer shoppingCart);
    void saveShoppingCart(ShoppingCarts shoppingCart);
    List<ShoppingBook> getShoppingBooksByShoppingCartId(Integer shoppingCartId);
}
