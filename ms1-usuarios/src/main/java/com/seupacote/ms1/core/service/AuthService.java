package com.seupacote.ms1.core.service;

import com.seupacote.ms1.configuration.exception.NotFoundException;
import com.seupacote.ms1.configuration.security.JwtUtil;
import com.seupacote.ms1.core.domain.User;
import com.seupacote.ms1.core.port.in.AuthServicePort;
import com.seupacote.ms1.core.port.out.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

/**
 * Caso de uso: autenticação de usuário.
 * Valida credenciais e gera token JWT.
 */
@Service
public class AuthService implements AuthServicePort {

    private final UserRepositoryPort repositoryPort;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthService(UserRepositoryPort repositoryPort,
                       PasswordEncoder passwordEncoder,
                       JwtUtil jwtUtil) {
        this.repositoryPort = repositoryPort;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public String login(String email, String senha) {
        User user = repositoryPort.buscarPorEmail(email)
                // Mensagem genérica para não vazar se o e-mail existe ou não
                .orElseThrow(() -> new NotFoundException("Credenciais inválidas"));

        if (!passwordEncoder.matches(senha, user.getSenha())) {
            throw new NotFoundException("Credenciais inválidas");
        }

        return jwtUtil.gerarToken(user.getEmail());
    }
}

