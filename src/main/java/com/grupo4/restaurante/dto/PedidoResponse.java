
package com.grupo4.restaurante.dto;

import java.math.BigDecimal;
import java.time.OffsetDateTime;

public record PedidoResponse(
        Long id,
        OffsetDateTime fecha,
        String estado,
        BigDecimal total,
        Long clienteId,
        String nombreCliente
) {
}
