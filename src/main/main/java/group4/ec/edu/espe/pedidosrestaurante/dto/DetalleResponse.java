package group4.ec.edu.espe.pedidosrestaurante.dto;

import java.math.BigDecimal;

public record DetalleResponse(Long id, Long platoId, String platoNombre,
                              Integer cantidad, BigDecimal precioUnitario, BigDecimal subtotal) {}
