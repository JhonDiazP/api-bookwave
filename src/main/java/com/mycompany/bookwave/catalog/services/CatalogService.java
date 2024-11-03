package com.mycompany.bookwave.catalog.services;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Arrays;

import com.mycompany.bookwave.catalog.controllers.CatalogExternalApi;
import com.mycompany.bookwave.catalog.controllers.CatalogInternalApi;
import com.mycompany.bookwave.catalog.entities.Book;
import com.mycompany.bookwave.catalog.entities.BookCategory;
import com.mycompany.bookwave.catalog.entities.Category;
import com.mycompany.bookwave.catalog.repositories.BookCategoryRepository;
import com.mycompany.bookwave.catalog.repositories.BookRepository;
import com.mycompany.bookwave.catalog.repositories.CategoryRepository;
import com.mycompany.bookwave.common.DTOs.BookDTO;

import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;

@Service
public class CatalogService implements CatalogExternalApi, CatalogInternalApi {
    
    private final BookRepository bookRepository;
    private final CategoryRepository categoryRepository;
    private final BookCategoryRepository bookCategoryRepository;

    public CatalogService(BookRepository bookRepository, CategoryRepository categoryRepository, BookCategoryRepository bookCategoryRepository) {
        this.bookRepository = bookRepository;
        this.categoryRepository = categoryRepository;
        this.bookCategoryRepository = bookCategoryRepository;
    }

    @Override
    public ResponseEntity<Map<String, Object>> saveBook(BookDTO bookDTO) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();

        try {
            Book book = new Book();
            book.setIsbn(bookDTO.isbn());
            book.setTitle(bookDTO.title());
            book.setPrice(bookDTO.price());
            book.setAuthor(bookDTO.author());
            book.setAmount(bookDTO.amount());
            book.setDescription(bookDTO.description());
            Book save = bookRepository.save(book);
    
            String[] stringArray = bookDTO.categories().split(",");
            
            int[] array_categories = Arrays.stream(stringArray)
                                .mapToInt(Integer::parseInt)
                                .toArray();

            for (int category_id : array_categories) {
                Category category = categoryRepository.findById((int)category_id).orElseThrow(
                    () -> new EntityNotFoundException("Category not found")
                );
                BookCategory bookCategory = new BookCategory();
                bookCategory.setBook(book);
                bookCategory.setCategory(category);
                bookCategoryRepository.save(bookCategory);
            }
            map.put("status", "success");
            map.put("message", "Book saved successfully");
            map.put("data", save);
        } catch (Exception e) {
            map.put("status", "error");
            map.put("message", "Error saving book");
            map.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(map);
    }

    @Override
    public ResponseEntity<Map<String, Object>> updateBook(BookDTO bookDTO, String id) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        try {
            Book book = bookRepository.findById(id).orElseThrow(
                () -> new EntityNotFoundException("Book not found")
            );
            book.setIsbn(bookDTO.isbn());
            book.setTitle(bookDTO.title());
            book.setPrice(bookDTO.price());
            book.setAuthor(bookDTO.author());
            book.setAmount(bookDTO.amount());
            book.setDescription(bookDTO.description());
            Book save = bookRepository.save(book);
            map.put("status", "success");
            map.put("message", "Book updated successfully");
            map.put("data", save);
            return ResponseEntity.status(HttpStatus.OK).body(map);
        } catch (Exception e) {
            map.put("status", "error");
            map.put("message", "Error updating book");
            map.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(map);
        }
    }

    @Override
    public ResponseEntity<Map<String, Object>> getBooks(String search) {
        Map<String, Object> map = new LinkedHashMap<String, Object>();
        map.put("status", "success");
        map.put("message", "Books retrieved successfully");
        if(search != "") {
            map.put("data", bookRepository.findByTitleContaining(search));
            return ResponseEntity.status(HttpStatus.OK).body(map);
        }
        map.put("data", bookRepository.findAll());
        return ResponseEntity.status(HttpStatus.OK).body(map);
    }

    @Override
    public Book getBookById(String id) {
        return bookRepository.findById(id).orElseThrow(
            () -> new EntityNotFoundException("Book not found")
        );
    }

    @Override
    public void saveBook(Book book) {
        bookRepository.save(book);
    }
}
