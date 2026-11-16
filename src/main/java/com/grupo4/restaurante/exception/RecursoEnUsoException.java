package com.grupo4.restaurante.exception;

public class RecursoEnUsoException extends RuntimeException {

    public RecursoEnUsoException(String mensaje) {
        super(mensaje);
    }
}