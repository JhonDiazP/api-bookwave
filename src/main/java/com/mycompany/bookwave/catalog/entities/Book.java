package com.mycompany.bookwave.catalog.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import java.math.BigDecimal;

@Entity
@Data
@Table(name = "books")
public class Book {  
    @Id
    private String isbn;  

    @Column(name = "title", nullable = false, length = 200)
    private String title;

    @Column(name = "price", nullable = false, length = 50)
    private BigDecimal price;

    @Column(name = "author", nullable = false, length = 100)
    private String author;

    @Column(name = "image_url", nullable = true, length = 255)
    private String imageURL;

    @Column(name = "amount", nullable = false, length = 4)
    private Integer amount;

    @Column(name = "description", nullable = false, length = 255)
    private String description;
}   
