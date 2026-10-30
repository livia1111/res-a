package com.seupacote.ms1.core.service;

import com.seupacote.ms1.configuration.exception.NotFoundException;
import com.seupacote.ms1.configuration.security.JwtUtil;
import com.seupacote.ms1.core.domain.User;
import com.seupacote.ms1.core.port.out.UserRepositoryPort;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepositoryPort repositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @InjectMocks
    private AuthService authService;

    @Test
    @DisplayName("Deve retornar token JWT com credenciais válidas")
    void deveRetornarTokenComCredenciaisValidas() {
        User user = new User("João", "joao@email.com", "$2a$10$hash");
        when(repositoryPort.buscarPorEmail("joao@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senha123", "$2a$10$hash")).thenReturn(true);
        when(jwtUtil.gerarToken("joao@email.com")).thenReturn("mocked.jwt.token");

        String token = authService.login("joao@email.com", "senha123");

        assertThat(token).isEqualTo("mocked.jwt.token");
        verify(jwtUtil).gerarToken("joao@email.com");
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando e-mail não existe")
    void deveLancarNotFoundQuandoEmailInexistente() {
        when(repositoryPort.buscarPorEmail("inexistente@email.com")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> authService.login("inexistente@email.com", "qualquer"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Credenciais inválidas");

        verify(jwtUtil, never()).gerarToken(any());
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando senha é incorreta")
    void deveLancarNotFoundQuandoSenhaErrada() {
        User user = new User("João", "joao@email.com", "$2a$10$hash");
        when(repositoryPort.buscarPorEmail("joao@email.com")).thenReturn(Optional.of(user));
        when(passwordEncoder.matches("senhaErrada", "$2a$10$hash")).thenReturn(false);

        assertThatThrownBy(() -> authService.login("joao@email.com", "senhaErrada"))
                .isInstanceOf(NotFoundException.class)
                .hasMessage("Credenciais inválidas");

        verify(jwtUtil, never()).gerarToken(any());
    }
}

