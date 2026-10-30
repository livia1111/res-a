package com.seupacote.ms1.adapter.out.persistence;

import com.seupacote.ms1.core.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

/**
 * Interface Spring Data JPA — detalhe de infraestrutura.
 * Fica no adaptador secundário (driven adapter), isolada do core.
 */
public interface UserJpaRepository extends JpaRepository<User, String> {

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

