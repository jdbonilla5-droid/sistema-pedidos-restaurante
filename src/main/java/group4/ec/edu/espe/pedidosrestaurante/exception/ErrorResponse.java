package group4.ec.edu.espe.pedidosrestaurante.exception;

import java.time.LocalDateTime;

public record ErrorResponse(LocalDateTime timestamp, int status, String error,
                            String mensaje, String path) {}
