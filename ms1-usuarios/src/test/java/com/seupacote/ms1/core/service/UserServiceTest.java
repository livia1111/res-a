package com.seupacote.ms1.core.service;

import com.seupacote.ms1.configuration.exception.ConflictException;
import com.seupacote.ms1.configuration.exception.NotFoundException;
import com.seupacote.ms1.core.domain.User;
import com.seupacote.ms1.core.port.out.UserRepositoryPort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

/**
 * Testes unitários do UserService.
 * Usa Mockito para isolar o service das dependências externas.
 */
@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepositoryPort repositoryPort;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User userFake;

    @BeforeEach
    void setUp() {
        userFake = new User("João Silva", "joao@email.com", "$2a$10$hashBcrypt");
        // Simula que o id foi gerado pelo banco
        setId(userFake, "uuid-gerado-123");
    }

    // ─── criarUsuario ─────────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve criar usuário quando e-mail não está cadastrado")
    void deveCriarUsuarioComSucesso() {
        when(repositoryPort.existePorEmail("joao@email.com")).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("$2a$10$hashBcrypt");
        when(repositoryPort.salvar(any(User.class))).thenReturn(userFake);

        User resultado = userService.criarUsuario(new User("João Silva", "joao@email.com", "senha123"));

        assertThat(resultado).isNotNull();
        assertThat(resultado.getNome()).isEqualTo("João Silva");
        assertThat(resultado.getEmail()).isEqualTo("joao@email.com");

        verify(passwordEncoder).encode("senha123");
        verify(repositoryPort).salvar(any(User.class));
    }

    @Test
    @DisplayName("Deve lançar ConflictException quando e-mail já está cadastrado")
    void deveLancarConflictQuandoEmailDuplicado() {
        when(repositoryPort.existePorEmail("joao@email.com")).thenReturn(true);

        assertThatThrownBy(() ->
                userService.criarUsuario(new User("João", "joao@email.com", "senha123"))
        )
        .isInstanceOf(ConflictException.class)
        .hasMessageContaining("joao@email.com");

        verify(repositoryPort, never()).salvar(any());
    }

    // ─── listarUsuarios ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve retornar lista de todos os usuários")
    void deveListarUsuarios() {
        User user2 = new User("Maria", "maria@email.com", "hash");
        when(repositoryPort.buscarTodos()).thenReturn(List.of(userFake, user2));

        List<User> resultado = userService.listarUsuarios();

        assertThat(resultado).hasSize(2);
        verify(repositoryPort).buscarTodos();
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando não há usuários")
    void deveRetornarListaVazia() {
        when(repositoryPort.buscarTodos()).thenReturn(List.of());

        List<User> resultado = userService.listarUsuarios();

        assertThat(resultado).isEmpty();
    }

    // ─── buscarPorId ──────────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve retornar usuário quando ID existe")
    void deveBuscarUsuarioPorId() {
        when(repositoryPort.buscarPorId("uuid-gerado-123")).thenReturn(Optional.of(userFake));

        User resultado = userService.buscarPorId("uuid-gerado-123");

        assertThat(resultado).isNotNull();
        assertThat(resultado.getEmail()).isEqualTo("joao@email.com");
    }

    @Test
    @DisplayName("Deve lançar NotFoundException quando ID não existe")
    void deveLancarNotFoundQuandoIdInexistente() {
        when(repositoryPort.buscarPorId("id-invalido")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.buscarPorId("id-invalido"))
                .isInstanceOf(NotFoundException.class)
                .hasMessageContaining("id-invalido");
    }

    // ─── atualizarUsuario ─────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve atualizar dados do usuário existente")
    void deveAtualizarUsuario() {
        User dadosNovos = new User("João Atualizado", "novo@email.com", "novaSenha");
        when(repositoryPort.buscarPorId("uuid-gerado-123")).thenReturn(Optional.of(userFake));
        when(passwordEncoder.encode("novaSenha")).thenReturn("$2a$10$novoHash");
        when(repositoryPort.salvar(any())).thenReturn(userFake);

        User resultado = userService.atualizarUsuario("uuid-gerado-123", dadosNovos);

        assertThat(resultado).isNotNull();
        verify(repositoryPort).salvar(any());
        verify(passwordEncoder).encode("novaSenha");
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao atualizar ID inexistente")
    void deveLancarNotFoundAoAtualizar() {
        when(repositoryPort.buscarPorId("id-invalido")).thenReturn(Optional.empty());

        assertThatThrownBy(() ->
                userService.atualizarUsuario("id-invalido", new User("X", "x@x.com", "123456"))
        ).isInstanceOf(NotFoundException.class);

        verify(repositoryPort, never()).salvar(any());
    }

    // ─── deletarUsuario ───────────────────────────────────────────────────────

    @Test
    @DisplayName("Deve deletar usuário existente sem exceção")
    void deveDeletarUsuario() {
        when(repositoryPort.buscarPorId("uuid-gerado-123")).thenReturn(Optional.of(userFake));

        assertThatCode(() -> userService.deletarUsuario("uuid-gerado-123"))
                .doesNotThrowAnyException();

        verify(repositoryPort).deletar("uuid-gerado-123");
    }

    @Test
    @DisplayName("Deve lançar NotFoundException ao deletar ID inexistente")
    void deveLancarNotFoundAoDeletar() {
        when(repositoryPort.buscarPorId("nao-existe")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.deletarUsuario("nao-existe"))
                .isInstanceOf(NotFoundException.class);

        verify(repositoryPort, never()).deletar(any());
    }

    // ─── Helpers ──────────────────────────────────────────────────────────────

    /** Usa reflexão para setar o id (campo gerado pelo JPA em produção) */
    private void setId(User user, String id) {
        try {
            var field = User.class.getDeclaredField("id");
            field.setAccessible(true);
            field.set(user, id);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

