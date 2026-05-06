package com.library.controller;

import com.library.BaseIntegrationTest;
import com.library.repository.UserRepository;
import com.library.service.UserService;
import com.library.dto.UserRegistrationDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes E2E para AuthController — Cadastro e Login de Usuários.
 *
 * ========================
 * TIPO: Caixa Preta + E2E (Controller)
 * ========================
 * - Testa os fluxos HTTP de registro e autenticação
 * - MongoDB real via Testcontainers (sem mocks)
 * - MockMvc simula requisições HTTP sem servidor real
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("AuthController — Testes E2E (Caixa Preta)")
class AuthControllerE2ETest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
    }

    // ========================
    // GET /login
    // ========================

    @Test
    @DisplayName("GET /login — Deve retornar página de login (HTTP 200, view correta)")
    void shouldShowLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    // ========================
    // GET /register
    // ========================

    @Test
    @DisplayName("GET /register — Deve exibir formulário de cadastro com DTO vazio")
    void shouldShowRegisterFormWithEmptyDTO() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("user"));
    }

    // ========================
    // POST /register — Casos de Sucesso
    // ========================

    @Test
    @DisplayName("POST /register — Deve criar conta e redirecionar para login com flash message")
    void shouldRegisterAndRedirectToLogin() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("name", "João Silva")
                        .param("username", "joaosilva")
                        .param("email", "joao@test.com")
                        .param("password", "senha123")
                        .param("confirmPassword", "senha123"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/login"));
    }

    // ========================
    // POST /register — Casos de Erro (Validação)
    // ========================

    @Test
    @DisplayName("POST /register — Deve retornar form com erros quando campos obrigatórios estão vazios")
    void shouldReturnFormWhenRequiredFieldsEmpty() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("name", "")
                        .param("username", "")
                        .param("email", "")
                        .param("password", "")
                        .param("confirmPassword", ""))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("POST /register — Deve retornar erro quando senhas não conferem")
    void shouldReturnErrorWhenPasswordsDontMatch() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("name", "Ana Lima")
                        .param("username", "analima")
                        .param("email", "ana@test.com")
                        .param("password", "senha123")
                        .param("confirmPassword", "outrasenha"))  // ← diferente
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("error"));
    }

    @Test
    @DisplayName("POST /register — Deve retornar erro para email inválido")
    void shouldReturnErrorForInvalidEmail() throws Exception {
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("name", "Pedro Costa")
                        .param("username", "pedro")
                        .param("email", "email-invalido")   // ← sem @
                        .param("password", "senha123")
                        .param("confirmPassword", "senha123"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().hasErrors());
    }

    @Test
    @DisplayName("POST /register — Deve retornar erro ao tentar cadastrar username duplicado")
    void shouldReturnErrorOnDuplicateUsername() throws Exception {
        // Cadastra o primeiro usuário
        userService.register(UserRegistrationDTO.builder()
                .name("Maria").username("maria").email("maria@test.com")
                .password("senha123").confirmPassword("senha123").build());

        // Tenta cadastrar com o mesmo username
        mockMvc.perform(post("/register")
                        .with(csrf())
                        .param("name", "Outra Maria")
                        .param("username", "maria")            // ← duplicado
                        .param("email", "outra@test.com")
                        .param("password", "senha123")
                        .param("confirmPassword", "senha123"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"))
                .andExpect(model().attributeExists("error"));
    }

    // ========================
    // Segurança — Rotas públicas
    // ========================

    @Test
    @DisplayName("GET /books — Deve redirecionar para login quando não autenticado")
    void shouldRedirectToLoginForProtectedRoute() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @DisplayName("GET / — Deve redirecionar para /books (que redireciona para login)")
    void shouldRedirectRootToBooks() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection());
    }
}
