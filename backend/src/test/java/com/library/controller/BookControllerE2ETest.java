package com.library.controller;

import com.library.BaseIntegrationTest;
import com.library.dto.UserRegistrationDTO;
import com.library.model.Book;
import com.library.repository.BookRepository;
import com.library.repository.UserRepository;
import com.library.service.BookService;
import com.library.service.UserService;
import com.library.dto.BookDTO;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes E2E (End-to-End) para BookController.
 *
 * ========================
 * TIPO: Caixa Preta (Black-Box) + E2E
 * ========================
 * - Caixa Preta: testa entradas HTTP e saídas HTTP sem olhar implementação interna
 * - E2E (Controller): testa o fluxo completo: HTTP Request → Controller → Service → DB → Response
 * - MockMvc: simula requisições HTTP sem subir servidor real (mais rápido)
 * - MongoDB real via Testcontainers (sem mocks de persistência)
 *
 * @WithMockUser: simula usuário autenticado no Spring Security sem precisar fazer login real
 *               Isso não é um "mock de negócio" — é infraestrutura de teste do próprio Spring Security.
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@DisplayName("BookController — Testes E2E (Caixa Preta)")
class BookControllerE2ETest extends BaseIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookService bookService;

    @Autowired
    private UserService userService;

    private String testOwnerId;

    @BeforeEach
    void setUp() {
        bookRepository.deleteAll();
        userRepository.deleteAll();

        // Cria usuário real no banco para os testes
        UserRegistrationDTO dto = UserRegistrationDTO.builder()
                .name("Usuário de Teste")
                .username("testuser")
                .email("test@library.com")
                .password("senha123")
                .confirmPassword("senha123")
                .build();
        var user = userService.register(dto);
        testOwnerId = user.getId();
    }

    // ========================
    // GET /books — Listagem
    // ========================

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("GET /books — Deve retornar página com lista de livros (HTTP 200)")
    void shouldReturn200WithBookList() throws Exception {
        // Cria um livro para exibir na lista
        bookService.create(
                BookDTO.builder().title("Java").author("Gosling").isbn("111")
                        .status(Book.ReadingStatus.LENDO).build(),
                testOwnerId);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())                          // HTTP 200
                .andExpect(view().name("books/list"))                // View correta
                .andExpect(model().attributeExists("books"))         // Model tem "books"
                .andExpect(content().string(org.hamcrest.Matchers.containsString("Java"))); // Título na página
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("GET /books — Deve suportar filtro por search (busca)")
    void shouldSupportSearchFilter() throws Exception {
        mockMvc.perform(get("/books").param("search", "clean"))
                .andExpect(status().isOk())
                .andExpect(model().attributeExists("books"));
    }

    @Test
    @DisplayName("GET /books — Deve redirecionar para login quando não autenticado (HTTP 302)")
    void shouldRedirectToLoginWhenNotAuthenticated() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrlPattern("**/login"));
    }

    // ========================
    // GET /books/new — Formulário de criação
    // ========================

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("GET /books/new — Deve exibir formulário de criação (HTTP 200)")
    void shouldShowCreateForm() throws Exception {
        mockMvc.perform(get("/books/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("books/form"))
                .andExpect(model().attributeExists("book"))
                .andExpect(model().attributeExists("statuses"));
    }

    // ========================
    // POST /books — Criação
    // ========================

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("POST /books — Deve criar livro e redirecionar (HTTP 302)")
    void shouldCreateBookAndRedirect() throws Exception {
        mockMvc.perform(post("/books")
                        .with(csrf())                           // CSRF token obrigatório
                        .param("title", "Effective Java")
                        .param("author", "Joshua Bloch")
                        .param("isbn", "978-0134686097")
                        .param("year", "2018")
                        .param("status", "QUERO_LER"))
                .andExpect(status().is3xxRedirection())         // Redirect após sucesso
                .andExpect(redirectedUrl("/books"));
    }

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("POST /books — Deve retornar formulário com erros quando título está vazio")
    void shouldReturnFormWithErrorsWhenTitleEmpty() throws Exception {
        mockMvc.perform(post("/books")
                        .with(csrf())
                        .param("title", "")                     // ← título vazio
                        .param("author", "Autor Válido")
                        .param("isbn", "978-0000000001"))
                .andExpect(status().isOk())                     // Volta para o form (não redireciona)
                .andExpect(view().name("books/form"))
                .andExpect(model().hasErrors());
    }

    // ========================
    // POST /books/{id}/delete — Exclusão
    // ========================

    @Test
    @WithMockUser(username = "testuser")
    @DisplayName("POST /books/{id}/delete — Deve excluir livro e redirecionar")
    void shouldDeleteBookAndRedirect() throws Exception {
        // Cria livro para deletar
        BookDTO created = bookService.create(
                BookDTO.builder().title("Para Deletar").author("Autor").isbn("del-isbn-001")
                        .status(Book.ReadingStatus.LIDO).build(),
                testOwnerId);

        mockMvc.perform(post("/books/" + created.getId() + "/delete")
                        .with(csrf()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/books"));

        // Verifica que foi removido do banco
        org.assertj.core.api.Assertions.assertThat(bookRepository.findAll()).isEmpty();
    }

    // ========================
    // Autenticação / Segurança
    // ========================

    @Test
    @DisplayName("GET /login — Deve exibir página de login (pública)")
    void shouldShowLoginPage() throws Exception {
        mockMvc.perform(get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/login"));
    }

    @Test
    @DisplayName("GET /register — Deve exibir página de cadastro (pública)")
    void shouldShowRegisterPage() throws Exception {
        mockMvc.perform(get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("auth/register"));
    }
}