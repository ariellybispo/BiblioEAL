package com.library.service;

import com.library.BaseIntegrationTest;
import com.library.dto.UserRegistrationDTO;
import com.library.model.User;
import com.library.repository.UserRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes de Integração para UserService.
 *
 * ========================
 * TIPO: Caixa Branca + Caixa Preta + Integração
 * ========================
 * - Caixa Branca: verifica que BCrypt é aplicado (não armazena senha em texto puro)
 * - Caixa Preta: testa entradas/saídas sem conhecer implementação interna
 * - Integração: MongoDB real via Testcontainers
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("UserService — Testes de Integração")
class UserServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private UserService userService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    // ========================
    // CADASTRO (REGISTER)
    // ========================

    @Test
    @DisplayName("Deve registrar usuário com sucesso quando dados são válidos")
    void shouldRegisterUserSuccessfully() {
        UserRegistrationDTO dto = buildRegistrationDTO("joao", "joao@email.com", "senha123");

        User registered = userService.register(dto);

        assertThat(registered.getId()).isNotNull();
        assertThat(registered.getUsername()).isEqualTo("joao");
        assertThat(registered.getEmail()).isEqualTo("joao@email.com");
        assertThat(registered.getRoles()).contains("ROLE_USER");
    }

    @Test
    @DisplayName("Deve armazenar senha com hash BCrypt (NUNCA em texto puro)")
    void shouldStorePasswordAsHash() {
        // Este é um teste de segurança crítico
        UserRegistrationDTO dto = buildRegistrationDTO("maria", "maria@email.com", "minhasenha");

        User registered = userService.register(dto);

        // A senha armazenada NÃO deve ser igual ao texto original
        assertThat(registered.getPassword()).isNotEqualTo("minhasenha");

        // Mas o BCrypt deve conseguir verificar que é a mesma senha
        assertThat(passwordEncoder.matches("minhasenha", registered.getPassword())).isTrue();
    }

    @Test
    @DisplayName("Deve lançar exceção quando username já está em uso")
    void shouldThrowExceptionOnDuplicateUsername() {
        userService.register(buildRegistrationDTO("pedro", "pedro@email.com", "senha123"));

        UserRegistrationDTO duplicate = buildRegistrationDTO("pedro", "outro@email.com", "senha456");

        assertThatThrownBy(() -> userService.register(duplicate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Username");
    }

    @Test
    @DisplayName("Deve lançar exceção quando email já está em uso")
    void shouldThrowExceptionOnDuplicateEmail() {
        userService.register(buildRegistrationDTO("ana", "compartilhado@email.com", "senha123"));

        UserRegistrationDTO duplicate = buildRegistrationDTO("outrouser", "compartilhado@email.com", "senha456");

        assertThatThrownBy(() -> userService.register(duplicate))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Email");
    }

    @Test
    @DisplayName("Deve lançar exceção quando senhas não conferem")
    void shouldThrowExceptionWhenPasswordsDontMatch() {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .name("Lucas")
                .username("lucas")
                .email("lucas@email.com")
                .password("senha123")
                .confirmPassword("senhaDiferente") // ← Diferente!
                .build();

        assertThatThrownBy(() -> userService.register(dto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("senhas");
    }

    // ========================
    // AUTENTICAÇÃO (Spring Security)
    // ========================

    @Test
    @DisplayName("Deve carregar usuário pelo username para autenticação (loadUserByUsername)")
    void shouldLoadUserByUsernameForAuthentication() {
        userService.register(buildRegistrationDTO("carlos", "carlos@email.com", "senha123"));

        // Spring Security chama este método durante o login
        UserDetails userDetails = userService.loadUserByUsername("carlos");

        assertThat(userDetails.getUsername()).isEqualTo("carlos");
        assertThat(userDetails.getAuthorities())
                .anyMatch(a -> a.getAuthority().equals("ROLE_USER"));
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException para usuário inexistente")
    void shouldThrowUsernameNotFoundExceptionForNonExistentUser() {
        assertThatThrownBy(() -> userService.loadUserByUsername("usuario-fantasma"))
                .isInstanceOf(UsernameNotFoundException.class);
    }

    // ========================
    // TESTES PARAMETRIZADOS — Múltiplos usuários
    // ========================

    @ParameterizedTest(name = "Deve registrar usuário: {0} / {1}")
    @CsvSource({
        "alice,   alice@test.com,   senha001",
        "bob,     bob@test.com,     senha002",
        "charlie, charlie@test.com, senha003"
    })
    @DisplayName("Deve registrar múltiplos usuários com dados distintos")
    void shouldRegisterMultipleUsers(String username, String email, String password) {
        UserRegistrationDTO dto = buildRegistrationDTO(username.trim(), email.trim(), password.trim());
        User registered = userService.register(dto);

        assertThat(registered.getUsername()).isEqualTo(username.trim());
        assertThat(registered.getEmail()).isEqualTo(email.trim());
        assertThat(registered.getId()).isNotNull();
    }

    // ========================
    // Método auxiliar
    // ========================

    private UserRegistrationDTO buildRegistrationDTO(String username, String email, String password) {
        return UserRegistrationDTO.builder()
                .name("Nome de " + username)
                .username(username)
                .email(email)
                .password(password)
                .confirmPassword(password)
                .build();
    }
}