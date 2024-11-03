package com.mycompany.bookwave.catalog.controllers;

import com.mycompany.bookwave.catalog.entities.Book;

public interface CatalogInternalApi {
    Book getBookById(String bookId);
    void saveBook(Book book);
}
