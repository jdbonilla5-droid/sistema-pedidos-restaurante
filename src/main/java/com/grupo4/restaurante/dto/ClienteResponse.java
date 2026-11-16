
package com.grupo4.restaurante.dto;

public record ClienteResponse(
        Long id,
        String nombre,
        String telefono,
        String correo
) {
}
