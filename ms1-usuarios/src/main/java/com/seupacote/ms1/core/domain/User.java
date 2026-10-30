package com.seupacote.ms1.core.domain;

import jakarta.persistence.*;

/**
 * Entidade de domínio User.
 * Anotações JPA aqui pois usamos Spring Data JPA como adaptador de persistência.
 * Validações Bean Validation pertencem ao DTO, NÃO à entidade.
 */
@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String nome;

    @Column(nullable = false, unique = true)
    private String email;

    /**
     * Senha SEMPRE armazenada como hash BCrypt.
     * Nunca persiste texto puro.
     */
    @Column(nullable = false)
    private String senha;

    // ─── Construtores ─────────────────────────────────────────────────────────

    public User() {}

    public User(String nome, String email, String senha) {
        this.nome = nome;
        this.email = email;
        this.senha = senha;
    }

    // ─── Getters e Setters ────────────────────────────────────────────────────

    public String getId() { return id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
}

