
package com.grupo4.restaurante.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedidoRequest(

        @NotNull(message = "El ID del cliente es obligatorio")
        @Positive(message = "El ID del cliente debe ser positivo")
        Long clienteId

) {
}
