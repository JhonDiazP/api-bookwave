package com.mycompany.bookwave.common.DTOs;

import jakarta.validation.constraints.NotBlank;
import java.math.BigDecimal;

public record BookDTO(
    @NotBlank(message = "ISBN no puede estar vacío")
    String isbn,
    @NotBlank(message = "El título no puede estar vacío")
    String title,
    @NotBlank(message = "El precio no puede estar vacío")
    BigDecimal price,
    @NotBlank(message = "El autor no puede estar vacío")
    String author,
    @NotBlank(message = "La imagen no puede estar vacía")
    String imageURL,
    @NotBlank(message = "La cantidad no puede estar vacía")
    Integer amount,
    @NotBlank(message = "La descripción no puede estar vacía")
    String description,
    @NotBlank(message = "La segunda imagen no puede estar vacía")
    String imageSecond,
    @NotBlank(message = "Debe tener al menos una categoría")
    String categories
) {
    
}
