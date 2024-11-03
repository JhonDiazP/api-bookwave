package com.mycompany.bookwave.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

import com.mycompany.bookwave.catalog.entities.Book;

public interface BookRepository extends JpaRepository<Book, String> {
    @Query("SELECT b FROM Book b WHERE b.title LIKE %?1%")
    List<Book> findByTitleContaining(String title);
}
