package com.mycompany.bookwave.common.DTOs;

import jakarta.validation.constraints.NotBlank;

public record ShoppingBookDTO(
    @NotBlank(message = "User id is required")
    String userId,
    @NotBlank(message = "Book id is required")
    String bookId
) {
    
}
