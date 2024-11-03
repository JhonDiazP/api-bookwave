package com.mycompany.bookwave.catalog.controllers;

import org.springframework.http.ResponseEntity;

import com.mycompany.bookwave.common.DTOs.BookDTO;

import java.util.Map;

public interface CatalogExternalApi {
    ResponseEntity<Map<String,Object>> saveBook(BookDTO book);
    ResponseEntity<Map<String,Object>> updateBook(BookDTO book, String id);
    ResponseEntity<Map<String,Object>> getBooks(String search);
}
