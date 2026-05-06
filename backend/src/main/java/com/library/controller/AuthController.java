package com.library.controller;

import com.library.dto.UserRegistrationDTO;
import com.library.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller para autenticação: login e cadastro de usuários.
 *
 * Padrão MVC:
 * - GET /login  → exibe página de login (gerenciada pelo Spring Security)
 * - GET /register → exibe formulário de cadastro
 * - POST /register → processa o cadastro
 *
 * O POST /login é tratado automaticamente pelo Spring Security (SecurityConfig).
 * Aqui apenas configuramos as páginas de exibição.
 */
@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * GET /login — Exibe a página de login.
     *
     * O Spring Security trata o POST /login automaticamente.
     * O parâmetro ?error indica falha de autenticação.
     * O parâmetro ?logout indica que o usuário saiu.
     */
    @GetMapping("/login")
    public String showLoginPage() {
        return "auth/login";  // → templates/auth/login.html
    }

    /**
     * GET /register — Exibe o formulário de cadastro.
     */
    @GetMapping("/register")
    public String showRegisterForm(Model model) {
        model.addAttribute("user", new UserRegistrationDTO());
        return "auth/register";  // → templates/auth/register.html
    }

    /**
     * POST /register — Processa o cadastro de um novo usuário.
     *
     * Fluxo:
     * 1. Valida os campos do formulário (Bean Validation)
     * 2. Chama UserService.register()
     * 3. Em caso de sucesso: redireciona para /login com mensagem
     * 4. Em caso de erro: volta para o formulário com a mensagem de erro
     */
    @PostMapping("/register")
    public String processRegistration(
            @Valid @ModelAttribute("user") UserRegistrationDTO dto,
            BindingResult result,
            Model model,
            RedirectAttributes redirectAttributes) {

        // Verifica erros de Bean Validation (campos obrigatórios, formato, etc.)
        if (result.hasErrors()) {
            return "auth/register";
        }

        // Verifica se as senhas conferem (regra de negócio)
        if (!dto.passwordsMatch()) {
            model.addAttribute("error", "As senhas não conferem");
            return "auth/register";
        }

        try {
            userService.register(dto);
            // Flash attribute: mensagem que sobrevive ao redirect
            redirectAttributes.addFlashAttribute("success",
                    "Conta criada com sucesso! Faça o login.");
            return "redirect:/login";
        } catch (IllegalArgumentException e) {
            // Username ou email já em uso
            model.addAttribute("error", e.getMessage());
            return "auth/register";
        }
    }
}
