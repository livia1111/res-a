package com.seupacote.ms1.adapter.out.persistence;

import com.seupacote.ms1.core.domain.User;
import com.seupacote.ms1.core.port.out.UserRepositoryPort;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

/**
 * Adaptador secundário — implementa UserRepositoryPort usando Spring Data JPA.
 * O core nunca enxerga esta classe diretamente.
 */
@Component
public class UserRepositoryAdapter implements UserRepositoryPort {

    private final UserJpaRepository jpaRepository;

    public UserRepositoryAdapter(UserJpaRepository jpaRepository) {
        this.jpaRepository = jpaRepository;
    }

    @Override
    public User salvar(User user) {
        return jpaRepository.save(user);
    }

    @Override
    public List<User> buscarTodos() {
        return jpaRepository.findAll();
    }

    @Override
    public Optional<User> buscarPorId(String id) {
        return jpaRepository.findById(id);
    }

    @Override
    public boolean existePorEmail(String email) {
        return jpaRepository.existsByEmail(email);
    }

    @Override
    public void deletar(String id) {
        jpaRepository.deleteById(id);
    }

    @Override
    public Optional<User> buscarPorEmail(String email) {
        return jpaRepository.findByEmail(email);
    }
}
