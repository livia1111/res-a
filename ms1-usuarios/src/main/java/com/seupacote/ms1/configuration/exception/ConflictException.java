package com.seupacote.ms1.configuration.exception;

/**
 * Lançada quando há conflito de dados (ex: e-mail duplicado) → resulta em HTTP 409.
 */
public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}

