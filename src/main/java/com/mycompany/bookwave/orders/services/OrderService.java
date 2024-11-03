package com.mycompany.bookwave.orders.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.mycompany.bookwave.catalog.entities.Book;
import com.mycompany.bookwave.catalog.repositories.BookRepository;
import com.mycompany.bookwave.common.DTOs.ShoppingBookDTO;
import com.mycompany.bookwave.orders.controllers.OrderExternalApi;
import com.mycompany.bookwave.orders.controllers.OrderInternalApi;
import com.mycompany.bookwave.orders.entities.ShoppingBook;
import com.mycompany.bookwave.orders.entities.ShoppingCartStatus;
import com.mycompany.bookwave.orders.entities.ShoppingCarts;
import com.mycompany.bookwave.orders.repositories.ShoppingBooksRepository;
import com.mycompany.bookwave.orders.repositories.ShoppingCartRepository;
import com.mycompany.bookwave.users.entities.User;

import jakarta.persistence.EntityNotFoundException;

@Service
public class OrderService implements OrderExternalApi, OrderInternalApi {
    private final ShoppingCartRepository shoppingCartsRepository;
    private final ShoppingBooksRepository shoppingBooksRepository;
    private final BookRepository bookRepository;

    public OrderService(ShoppingCartRepository shoppingCartsRepository, 
                        ShoppingBooksRepository shoppingBooksRepository,
                        BookRepository bookRepository
                        ) {
        this.shoppingCartsRepository = shoppingCartsRepository;
        this.shoppingBooksRepository = shoppingBooksRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public ResponseEntity<Map<String, Object>> addShoppingCard(ShoppingBookDTO memberShipCardDTO) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            ShoppingBook shoppingBook = new ShoppingBook();
            Book book = bookRepository.findById(memberShipCardDTO.bookId()).orElseThrow(
                () -> new EntityNotFoundException("Book not found")
            );
            shoppingBook.setBook(book);
            Integer shoppingCartsId = this.findByUser(memberShipCardDTO.userId());
            ShoppingCarts shoppingCarts = shoppingCartsRepository.findById(shoppingCartsId).orElseThrow(
                () -> new EntityNotFoundException("Shopping cart not found")
            );
            shoppingBook.setShoppingCarts(shoppingCarts);
            shoppingBooksRepository.save(shoppingBook);

            Integer status = this.findByStatusByUser(memberShipCardDTO.userId());
            if(status != null) {
                if (status == 2 || status == 3) {
                    this.changeStatusShoppingCard(shoppingCartsId);
                }
            }
            map.put("status", true);
            map.put("message", "Book added to shopping cart");
            map.put("data", shoppingBook);
            return new ResponseEntity<>(map, HttpStatus.CREATED);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("status", false);
            map.put("message", "Error adding book to shopping cart, error: "+e.getMessage());
            return new ResponseEntity<>(map, HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @Override
    public void createShoppingCard(User user) {
        try {
            ShoppingCarts shoppingCarts = new ShoppingCarts();
            shoppingCarts.setUser(user);
            ShoppingCartStatus status = new ShoppingCartStatus();
            status.setId(1);
            shoppingCarts.setStatus(status);
            shoppingCartsRepository.save(shoppingCarts);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<Book> getBooksInShoppingCard(Integer shoppingCart) {
        return shoppingBooksRepository.findByShoppingCartsId(shoppingCart);
    }

    public void changeStatusShoppingCard(Integer shoppingCartsId) {
        try {
            ShoppingCarts shoppingCarts = shoppingCartsRepository.findById(shoppingCartsId).get();
            ShoppingCartStatus status = new ShoppingCartStatus();
            status.setId(1);
            shoppingCarts.setStatus(status);
            shoppingCartsRepository.save(shoppingCarts);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public ShoppingCarts getShoppingCartById(Integer shoppingCart) {
        return shoppingCartsRepository.findById(shoppingCart).get();
    }

    @Override
    public void saveShoppingCart(ShoppingCarts shoppingCart) {
        shoppingCartsRepository.save(shoppingCart);
    }

    @Override
    public List<ShoppingBook> getShoppingBooksByShoppingCartId(Integer shoppingCartId) {
        return shoppingBooksRepository.findShoppingBooksByShoppingCartId(shoppingCartId);
    }

    public Integer findByStatusByUser(String userId) {
        return shoppingCartsRepository.getStatusByUserId(userId);
    }

    public Integer findByUser(String userId) {
        return shoppingCartsRepository.getByUserId(userId);
    }
}
