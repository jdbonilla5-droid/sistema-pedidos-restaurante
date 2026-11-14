package com.grupo4.restaurante.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Digits;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;

public record PlatoRequest(

        @NotBlank(message = "El nombre es obligatorio")
        @Size(max = 100, message = "El nombre no puede superar 100 caracteres")
        String nombre,

        @Size(max = 255, message = "La descripción no puede superar 255 caracteres")
        String descripcion,

        @NotNull(message = "El precio es obligatorio")
        @DecimalMin(value = "0.01", message = "El precio debe ser al menos 0.01")
        @Digits(integer = 10, fraction = 2,
                message = "El precio admite hasta 10 enteros y 2 decimales")
        BigDecimal precio,

        @NotNull(message = "La disponibilidad es obligatoria")
        Boolean disponible,

        @NotNull(message = "La categoría es obligatoria")
        @Positive(message = "El ID de la categoría debe ser positivo")
        Long categoriaId

) {
}