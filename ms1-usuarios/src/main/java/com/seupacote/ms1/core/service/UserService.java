package com.seupacote.ms1.core.service;

import com.seupacote.ms1.configuration.exception.ConflictException;
import com.seupacote.ms1.configuration.exception.NotFoundException;
import com.seupacote.ms1.core.domain.User;
import com.seupacote.ms1.core.port.in.UserServicePort;
import com.seupacote.ms1.core.port.out.UserRepositoryPort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementação dos casos de uso (hexágono central).
 * Depende apenas de interfaces (portas), nunca de implementações concretas.
 */
@Service
public class UserService implements UserServicePort {

    private final UserRepositoryPort repositoryPort;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepositoryPort repositoryPort, PasswordEncoder passwordEncoder) {
        this.repositoryPort = repositoryPort;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public User criarUsuario(User user) {
        if (repositoryPort.existePorEmail(user.getEmail())) {
            throw new ConflictException("E-mail já cadastrado: " + user.getEmail());
        }
        // Criptografa a senha antes de persistir
        user.setSenha(passwordEncoder.encode(user.getSenha()));
        return repositoryPort.salvar(user);
    }

    @Override
    public List<User> listarUsuarios() {
        return repositoryPort.buscarTodos();
    }

    @Override
    public User buscarPorId(String id) {
        return repositoryPort.buscarPorId(id)
                .orElseThrow(() -> new NotFoundException("Usuário não encontrado com id: " + id));
    }

    @Override
    public User atualizarUsuario(String id, User dadosAtualizados) {
        User existente = buscarPorId(id); // lança 404 se não existir

        existente.setNome(dadosAtualizados.getNome());
        existente.setEmail(dadosAtualizados.getEmail());

        // Só atualiza senha se uma nova foi enviada
        if (dadosAtualizados.getSenha() != null && !dadosAtualizados.getSenha().isBlank()) {
            existente.setSenha(passwordEncoder.encode(dadosAtualizados.getSenha()));
        }

        return repositoryPort.salvar(existente);
    }

    @Override
    public void deletarUsuario(String id) {
        buscarPorId(id); // garante que existe → lança 404 se não existir
        repositoryPort.deletar(id);
    }
}

