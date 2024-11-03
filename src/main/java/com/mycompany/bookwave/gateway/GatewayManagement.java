package com.mycompany.bookwave.gateway;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.mycompany.bookwave.catalog.controllers.CatalogExternalApi;
import com.mycompany.bookwave.common.DTOs.BookDTO;
import com.mycompany.bookwave.common.DTOs.BuyDTO;
import com.mycompany.bookwave.common.DTOs.MemberShipCardDTO;
import com.mycompany.bookwave.common.DTOs.ShoppingBookDTO;
import com.mycompany.bookwave.common.DTOs.UserDTO;
import com.mycompany.bookwave.orders.controllers.OrderExternalApi;
import com.mycompany.bookwave.payments.controllers.PaymentExternalApi;
import com.mycompany.bookwave.users.controllers.UserExternalApi;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/v1")
@Validated
public class GatewayManagement {
    
    private UserExternalApi userExternalApi;
    private CatalogExternalApi catalogExternalApi;
    private OrderExternalApi orderExternalApi;
    private PaymentExternalApi paymentExternalApi;

    public GatewayManagement(UserExternalApi userExternalApi, CatalogExternalApi catalogExternalApi, OrderExternalApi orderExternalApi, PaymentExternalApi paymentExternalApi) {
        this.userExternalApi = userExternalApi;
        this.catalogExternalApi = catalogExternalApi;
        this.orderExternalApi = orderExternalApi;
        this.paymentExternalApi = paymentExternalApi;
    }

    @PutMapping("/users/{id}")
    public ResponseEntity<Map<String,Object>> updateUser(@Valid @RequestBody UserDTO userDTO, @PathVariable("id") String id) {
        return userExternalApi.updateUser(userDTO, id);
    }

    @PostMapping("/books")
    public ResponseEntity<Map<String,Object>> saveBook(@Valid @RequestBody BookDTO bookDTO) {
        return catalogExternalApi.saveBook(bookDTO);
    }

    @PutMapping("/books/{id}")
    public ResponseEntity<Map<String,Object>> updateBook(@Valid @RequestBody BookDTO bookDTO, @PathVariable("id") String id) {
        return catalogExternalApi.updateBook(bookDTO, id);
    }

    @GetMapping("/books")
    public ResponseEntity<Map<String,Object>> getBooks(@RequestParam(name = "search") String search) {
        return catalogExternalApi.getBooks(search);
    }

    @PutMapping("/membership")
    public ResponseEntity<Map<String,Object>> updateMemberShipCard(@Valid @RequestBody MemberShipCardDTO memberShipCardDTO) {
        return userExternalApi.updateMemberShipCard(memberShipCardDTO);
    }

    @PostMapping("/shopping-book")
    public ResponseEntity<Map<String,Object>> addShoppingCard(@Valid @RequestBody ShoppingBookDTO memberShipCardDTO) {
        return orderExternalApi.addShoppingCard(memberShipCardDTO);
    }

    @PostMapping("/buy-books")
    public ResponseEntity<Map<String,Object>> buyBooks(@Valid @RequestBody BuyDTO buyDTO) {
        return paymentExternalApi.savePayment(buyDTO);
    }
}
