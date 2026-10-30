package com.seupacote.ms1.configuration.exception;

/**
 * Lançada quando um recurso não é encontrado → resulta em HTTP 404.
 */
public class NotFoundException extends RuntimeException {
    public NotFoundException(String message) {
        super(message);
    }
}

