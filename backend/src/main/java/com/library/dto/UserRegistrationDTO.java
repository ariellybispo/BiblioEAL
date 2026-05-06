package com.library.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para cadastro de novos usuários.
 *
 * Contém as validações Bean Validation para o formulário de registro.
 * O método passwordsMatch() é lógica de negócio pura — testada unitariamente
 * sem necessidade de Spring ou banco de dados.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserRegistrationDTO {

    @NotBlank(message = "Nome é obrigatório")
    private String name;

    @NotBlank(message = "Username é obrigatório")
    @Size(min = 3, max = 30, message = "Username deve ter entre 3 e 30 caracteres")
    private String username;

    @NotBlank(message = "Email é obrigatório")
    @Email(message = "Email inválido")
    private String email;

    @NotBlank(message = "Senha é obrigatória")
    @Size(min = 6, message = "Senha deve ter no mínimo 6 caracteres")
    private String password;

    @NotBlank(message = "Confirmação de senha é obrigatória")
    private String confirmPassword;

    /**
     * Verifica se as senhas informadas são iguais.
     *
     * Lógica de negócio: a confirmação de senha é uma regra do domínio,
     * não apenas uma validação de formulário.
     *
     * @return true se password e confirmPassword são iguais e não nulos
     */
    public boolean passwordsMatch() {
        if (password == null) {
            return false;
        }
        return password.equals(confirmPassword);
    }
}
