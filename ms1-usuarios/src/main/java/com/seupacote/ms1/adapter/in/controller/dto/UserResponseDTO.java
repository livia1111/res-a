package com.seupacote.ms1.adapter.in.controller.dto;

import com.seupacote.ms1.core.domain.User;

/**
 * DTO de SAÍDA — retornado nas respostas da API.
 * NÃO inclui a senha — nunca exponha dados sensíveis.
 */
public class UserResponseDTO {

    private String id;
    private String nome;
    private String email;

    // ─── Construtor de mapeamento (User → DTO) ────────────────────────────────

    public static UserResponseDTO fromDomain(User user) {
        UserResponseDTO dto = new UserResponseDTO();
        dto.id    = user.getId();
        dto.nome  = user.getNome();
        dto.email = user.getEmail();
        return dto;
    }

    // ─── Getters ──────────────────────────────────────────────────────────────

    public String getId()    { return id; }
    public String getNome()  { return nome; }
    public String getEmail() { return email; }
}

