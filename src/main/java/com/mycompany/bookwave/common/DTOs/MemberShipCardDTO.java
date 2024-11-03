package com.mycompany.bookwave.common.DTOs;

import java.time.LocalDate;
import java.math.BigDecimal;

import jakarta.validation.constraints.NotBlank;

public record MemberShipCardDTO(
    String id,
    @NotBlank(message = "El número de tarjeta no puede estar vacío")
    String cardNumber,
    @NotBlank(message = "La cuota no puede estar vacía")
    BigDecimal quota
) {
}
