
package com.grupo4.restaurante.dto;

import java.math.BigDecimal;

public record PedidoDetalleResponse(
        Long id,
        Long pedidoId,
        Long platoId,
        String nombrePlato,
        Integer cantidad,
        BigDecimal precioUnitario,
        BigDecimal subtotal
) {
}
