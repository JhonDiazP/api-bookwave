package com.mycompany.bookwave.payments.controllers;

import org.springframework.http.ResponseEntity;

import com.mycompany.bookwave.common.DTOs.BuyDTO;

import java.util.Map;

public interface PaymentExternalApi {
    ResponseEntity<Map<String,Object>> savePayment(BuyDTO buyDTO);
}
