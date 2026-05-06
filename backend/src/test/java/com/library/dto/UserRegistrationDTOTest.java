package com.library.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes Unitários para UserRegistrationDTO.
 *
 * ========================
 * TIPO: Caixa Branca (White-Box) — Método passwordsMatch()
 * ========================
 * Testa toda a lógica interna do método passwordsMatch():
 * - Senhas iguais → true
 * - Senhas diferentes → false
 * - Casos especiais: null, vazio, espaços
 */
@DisplayName("UserRegistrationDTO — Testes Unitários (Caixa Branca)")
class UserRegistrationDTOTest {

    @Test
    @DisplayName("passwordsMatch() deve retornar true quando as senhas são iguais")
    void shouldReturnTrueWhenPasswordsMatch() {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .password("minhasenha123")
                .confirmPassword("minhasenha123")
                .build();

        assertThat(dto.passwordsMatch()).isTrue();
    }

    @Test
    @DisplayName("passwordsMatch() deve retornar false quando as senhas são diferentes")
    void shouldReturnFalseWhenPasswordsDontMatch() {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .password("senha1")
                .confirmPassword("senha2")
                .build();

        assertThat(dto.passwordsMatch()).isFalse();
    }

    @Test
    @DisplayName("passwordsMatch() deve retornar false quando password é null")
    void shouldReturnFalseWhenPasswordIsNull() {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .password(null)
                .confirmPassword("qualquer")
                .build();

        assertThat(dto.passwordsMatch()).isFalse();
    }

    @Test
    @DisplayName("passwordsMatch() deve ser sensível a maiúsculas/minúsculas")
    void shouldBeCaseSensitive() {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .password("Senha123")
                .confirmPassword("senha123") // ← 's' minúsculo
                .build();

        // Senhas com capitalização diferente são DIFERENTES
        assertThat(dto.passwordsMatch()).isFalse();
    }

    @Test
    @DisplayName("passwordsMatch() deve ser sensível a espaços")
    void shouldBeSpaceSensitive() {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .password("senha 123")   // com espaço
                .confirmPassword("senha123") // sem espaço
                .build();

        assertThat(dto.passwordsMatch()).isFalse();
    }

    @ParameterizedTest(name = "Senha [{0}] == [{1}] deve ser {2}")
    @CsvSource({
        "abc123,   abc123,   true",
        "abc123,   ABC123,   false",
        "senha,    senhaX,   false",
        "12345678, 12345678, true"
    })
    @DisplayName("Deve validar corretamente múltiplos cenários de senha")
    void shouldValidateVariousPasswordScenarios(String password, String confirm, boolean expected) {
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .password(password.trim())
                .confirmPassword(confirm.trim())
                .build();

        assertThat(dto.passwordsMatch()).isEqualTo(expected);
    }
}