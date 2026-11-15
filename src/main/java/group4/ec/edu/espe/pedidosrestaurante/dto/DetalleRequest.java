package group4.ec.edu.espe.pedidosrestaurante.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record DetalleRequest(
        @NotNull(message = "El id del plato es obligatorio") Long platoId,
        @NotNull(message = "La cantidad es obligatoria")
        @Min(value = 1, message = "La cantidad debe ser mayor a 0") Integer cantidad) {}
