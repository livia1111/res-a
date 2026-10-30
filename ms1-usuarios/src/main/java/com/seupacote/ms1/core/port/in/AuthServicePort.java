package com.seupacote.ms1.core.port.in;

public interface AuthServicePort {
    /**
     * Autentica o usuário e retorna um token JWT.
     * Lança NotFoundException se as credenciais forem inválidas.
     */
    String login(String email, String senha);
}

