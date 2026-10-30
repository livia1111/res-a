package com.seupacote.ms1.core.port.in;

import com.seupacote.ms1.core.domain.User;

import java.util.List;

/**
 * Porta primária (driving port) — define os casos de uso do domínio.
 * O Controller depende DESTA interface, nunca do service diretamente.
 */
public interface UserServicePort {

    User criarUsuario(User user);

    List<User> listarUsuarios();

    User buscarPorId(String id);

    User atualizarUsuario(String id, User dadosAtualizados);

    void deletarUsuario(String id);
}

