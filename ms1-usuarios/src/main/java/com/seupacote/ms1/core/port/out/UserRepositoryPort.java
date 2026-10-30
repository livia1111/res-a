package com.seupacote.ms1.core.port.out;

import com.seupacote.ms1.core.domain.User;

import java.util.List;
import java.util.Optional;

/**
 * Porta secundária (driven port) — contrato que o adaptador de persistência deve implementar.
 * O Service depende DESTA interface, nunca do repositório JPA diretamente.
 */
public interface UserRepositoryPort {

    User salvar(User user);

    List<User> buscarTodos();

    Optional<User> buscarPorId(String id);

    boolean existePorEmail(String email);

    void deletar(String id);

    Optional<User> buscarPorEmail(String email);
}

