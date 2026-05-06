package com.library.repository;

import com.library.model.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório para operações de banco de dados de Usuários.
 */
@Repository
public interface UserRepository extends MongoRepository<User, String> {

    /**
     * Busca usuário pelo username para autenticação.
     * O Spring Security usa este método via UserDetailsService.
     */
    Optional<User> findByUsername(String username);

    /**
     * Busca usuário pelo email (para validação de duplicidade no cadastro).
     */
    Optional<User> findByEmail(String email);

    /**
     * Verifica existência por username (mais eficiente que buscar o objeto inteiro).
     */
    boolean existsByUsername(String username);

    /**
     * Verifica existência por email.
     */
    boolean existsByEmail(String email);
}