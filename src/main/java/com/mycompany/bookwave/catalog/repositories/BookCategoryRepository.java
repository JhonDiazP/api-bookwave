package com.mycompany.bookwave.catalog.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.mycompany.bookwave.catalog.entities.BookCategory;

public interface BookCategoryRepository extends JpaRepository<BookCategory, Integer> {
    
}
