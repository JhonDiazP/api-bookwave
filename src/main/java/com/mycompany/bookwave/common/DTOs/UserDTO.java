package com.mycompany.bookwave.common.DTOs;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record UserDTO(
        @NotBlank(message = "ID no puede estar vacío")
        String id,

        @NotBlank(message = "El nombre de usuario no puede estar vacío")
        String username,

        @NotBlank(message = "El nombre no puede estar vacío")
        String firstName,

        @NotBlank(message = "El apellido no puede estar vacío")
        String lastName,

        @NotNull(message = "El municipio no puede ser nulo")
        Integer municipality,

        @NotNull(message = "El género no puede ser nulo")
        Integer gender,

        String profession,

        @NotBlank(message = "El email no puede estar vacío")
        String email,

        String phone,

        @NotBlank(message = "La contraseña no puede estar vacía")
        String password,

        @NotNull(message = "El estado no puede ser nulo")
        Integer status,

        @NotBlank(message = "La tarjeta de membresía no puede estar vacía")
        String memberShipCard
) {
    
}
