package com.mycompany.bookwave.orders.controllers;

import java.util.Map;
import org.springframework.http.ResponseEntity;
import com.mycompany.bookwave.common.DTOs.ShoppingBookDTO;

public interface OrderExternalApi {
    ResponseEntity<Map<String,Object>> addShoppingCard(ShoppingBookDTO shoppingBookDTO);
}
