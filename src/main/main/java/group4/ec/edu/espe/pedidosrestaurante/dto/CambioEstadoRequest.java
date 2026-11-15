package group4.ec.edu.espe.pedidosrestaurante.dto;

import group4.ec.edu.espe.pedidosrestaurante.entity.EstadoPedido;
import jakarta.validation.constraints.NotNull;

public record CambioEstadoRequest(
        @NotNull(message = "El estado es obligatorio") EstadoPedido estado) {}
