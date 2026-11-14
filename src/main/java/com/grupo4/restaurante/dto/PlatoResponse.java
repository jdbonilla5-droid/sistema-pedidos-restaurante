package com.grupo4.restaurante.dto;

import java.math.BigDecimal;

public record PlatoResponse(
        Long id,
        String nombre,
        String descripcion,
        BigDecimal precio,
        boolean disponible,
        Long categoriaId,
        String categoriaNombre
) {
}