package com.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Entidade User — representa um usuário cadastrado no sistema.
 *
 * @Document: mapeia para a coleção "users" no MongoDB.
 * @Indexed(unique = true): cria índice único no MongoDB, garantindo
 *   que dois usuários não possam ter o mesmo username ou email.
 *   Este índice é respeitado pelos Testcontainers (MongoDB real),
 *   mas seria ignorado por mocks.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "users")
public class User {

    @Id
    private String id;

    /** Nome completo do usuário */
    private String name;

    /**
     * Username único para login.
     * @Indexed(unique = true): garante unicidade no banco.
     */
    @Indexed(unique = true)
    private String username;

    /**
     * Email único do usuário.
     */
    @Indexed(unique = true)
    private String email;

    /**
     * Senha armazenada como hash BCrypt.
     * NUNCA armazenamos senha em texto puro.
     */
    private String password;

    /**
     * Papéis/permissões do usuário para o Spring Security.
     * Ex: ["ROLE_USER"], ["ROLE_USER", "ROLE_ADMIN"]
     */
    private List<String> roles;

    /** Data de cadastro */
    private LocalDateTime createdAt;
}
