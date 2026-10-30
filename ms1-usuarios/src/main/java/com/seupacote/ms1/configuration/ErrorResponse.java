package com.seupacote.ms1.configuration;

import java.time.LocalDateTime;

/**
 * Objeto de resposta de erro padronizado.
 * Retornado em todas as respostas de erro (4xx).
 */
public class ErrorResponse {

    private int status;
    private String mensagem;
    private LocalDateTime timestamp;

    public ErrorResponse(int status, String mensagem) {
        this.status    = status;
        this.mensagem  = mensagem;
        this.timestamp = LocalDateTime.now();
    }

    public int getStatus()              { return status; }
    public String getMensagem()         { return mensagem; }
    public LocalDateTime getTimestamp() { return timestamp; }
}

