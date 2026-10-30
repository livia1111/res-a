package com.seupacote.ms1.adapter.in.controller.dto;

public class LoginResponseDTO {

    private final String token;
    private final String tipo = "Bearer";

    public LoginResponseDTO(String token) {
        this.token = token;
    }

    public String getToken() { return token; }
    public String getTipo()  { return tipo; }
}

