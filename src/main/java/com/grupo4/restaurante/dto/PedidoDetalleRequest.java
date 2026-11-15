
package com.grupo4.restaurante.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;

public record PedidoDetalleRequest(

        @NotNull(message = "El ID del plato es obligatorio")
        @Positive(message = "El ID del plato debe ser positivo")
        Long platoId,

        @NotNull(message = "La cantidad es obligatoria")
        @Positive(message = "La cantidad debe ser mayor que cero")
        Integer cantidad

) {
}
