package com.library.service;

import com.library.BaseIntegrationTest;
import com.library.dto.BookDTO;
import com.library.model.Book;
import com.library.repository.BookRepository;
import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes de Integração para BookService.
 *
 * ========================
 * TIPO: Caixa Branca + Integração com Testcontainers
 * ========================
 * - Caixa Branca: testa a lógica interna do Service (regras de negócio)
 * - Integração: usa MongoDB real via Testcontainers (sem mocks)
 *
 * Cada método @Test é independente — o @BeforeEach limpa o banco.
 */
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("BookService — Testes de Integração")
class BookServiceIntegrationTest extends BaseIntegrationTest {

    @Autowired
    private BookService bookService;

    @Autowired
    private BookRepository bookRepository;

    private static final String OWNER_ID = "user-teste-001";
    private static final String OTHER_OWNER_ID = "user-outro-002";

    @BeforeEach
    void setUp() {
        // Estado limpo antes de cada teste — evita interferência entre testes
        bookRepository.deleteAll();
    }

    // ========================
    // CRIAÇÃO (CREATE)
    // ========================

    @Test
    @DisplayName("Deve criar livro com sucesso quando dados são válidos")
    void shouldCreateBookSuccessfully() {
        // ARRANGE — prepara os dados de entrada
        BookDTO dto = buildBookDTO("Clean Code", "Robert Martin", "978-0132350884");

        // ACT — executa a operação
        BookDTO created = bookService.create(dto, OWNER_ID);

        // ASSERT — verifica o resultado
        assertThat(created.getId()).isNotNull();
        assertThat(created.getTitle()).isEqualTo("Clean Code");
        assertThat(created.getAuthor()).isEqualTo("Robert Martin");
        assertThat(created.getIsbn()).isEqualTo("978-0132350884");
    }

    @Test
    @DisplayName("Deve lançar exceção ao criar livro com ISBN duplicado para o mesmo usuário")
    void shouldThrowExceptionOnDuplicateIsbnSameOwner() {
        // ARRANGE — cria o primeiro livro com o ISBN
        BookDTO first = buildBookDTO("Clean Code", "Robert Martin", "978-0132350884");
        bookService.create(first, OWNER_ID);

        // Tenta criar outro livro com o mesmo ISBN
        BookDTO duplicate = buildBookDTO("Outro Livro", "Outro Autor", "978-0132350884");

        // ACT & ASSERT — espera a exceção específica
        assertThatThrownBy(() -> bookService.create(duplicate, OWNER_ID))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("ISBN");
    }

    @Test
    @DisplayName("Deve permitir ISBN duplicado para usuários DIFERENTES (isolamento de dados)")
    void shouldAllowSameIsbnForDifferentOwners() {
        // Esta é uma regra importante: ISBN é único POR usuário, não globalmente
        BookDTO dto1 = buildBookDTO("Clean Code", "Robert Martin", "978-0132350884");
        BookDTO dto2 = buildBookDTO("Clean Code", "Robert Martin", "978-0132350884");

        BookDTO created1 = bookService.create(dto1, OWNER_ID);
        BookDTO created2 = bookService.create(dto2, OTHER_OWNER_ID);

        // Ambos devem ser criados com sucesso
        assertThat(created1.getId()).isNotNull();
        assertThat(created2.getId()).isNotNull();
        assertThat(created1.getId()).isNotEqualTo(created2.getId());
    }

    @Test
    @DisplayName("Deve definir status padrão QUERO_LER quando não informado")
    void shouldSetDefaultStatusWhenNotProvided() {
        BookDTO dto = buildBookDTO("TDD", "Kent Beck", "978-0321146533");
        dto.setStatus(null); // Status não informado

        BookDTO created = bookService.create(dto, OWNER_ID);

        assertThat(created.getStatus()).isEqualTo(Book.ReadingStatus.QUERO_LER);
    }

    // ========================
    // LISTAGEM (READ)
    // ========================

    @Test
    @DisplayName("Deve listar apenas os livros do próprio usuário")
    void shouldListOnlyOwnerBooks() {
        // Cria livros para dois usuários diferentes
        bookService.create(buildBookDTO("Livro do User 1", "Autor A", "111"), OWNER_ID);
        bookService.create(buildBookDTO("Livro do User 2", "Autor B", "222"), OTHER_OWNER_ID);

        List<BookDTO> ownerBooks = bookService.findAllByOwner(OWNER_ID);

        // Deve retornar apenas 1 livro (do OWNER_ID)
        assertThat(ownerBooks).hasSize(1);
        assertThat(ownerBooks.get(0).getTitle()).isEqualTo("Livro do User 1");
    }

    @Test
    @DisplayName("Deve retornar lista vazia quando usuário não tem livros")
    void shouldReturnEmptyListWhenNoBooks() {
        List<BookDTO> books = bookService.findAllByOwner("usuario-sem-livros");
        assertThat(books).isEmpty();
    }

    @Test
    @DisplayName("Deve lançar exceção ao buscar livro que pertence a outro usuário (segurança)")
    void shouldThrowExceptionWhenAccessingAnotherUserBook() {
        BookDTO created = bookService.create(
                buildBookDTO("Livro Privado", "Autor", "999"), OWNER_ID);

        // Outro usuário tenta acessar o livro pelo ID
        assertThatThrownBy(() -> bookService.findByIdAndOwner(created.getId(), OTHER_OWNER_ID))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ========================
    // BUSCA (SEARCH)
    // ========================

    @Test
    @DisplayName("Deve buscar livros por título de forma case-insensitive")
    void shouldSearchBooksIgnoringCase() {
        bookService.create(buildBookDTO("Clean Code", "Robert Martin", "111"), OWNER_ID);
        bookService.create(buildBookDTO("Clean Architecture", "Robert Martin", "222"), OWNER_ID);
        bookService.create(buildBookDTO("Refactoring", "Martin Fowler", "333"), OWNER_ID);

        // Busca com minúsculas, mas os títulos têm maiúsculas
        List<BookDTO> results = bookService.searchByTitle("clean", OWNER_ID);

        assertThat(results).hasSize(2);
        assertThat(results).extracting(BookDTO::getTitle)
                .containsExactlyInAnyOrder("Clean Code", "Clean Architecture");
    }

    // ========================
    // TESTES PARAMETRIZADOS — Filtro por Status
    // ========================

    @ParameterizedTest(name = "Deve filtrar livros com status: {0}")
    @EnumSource(Book.ReadingStatus.class)
    @DisplayName("Deve filtrar livros por cada status possível")
    void shouldFilterByEachReadingStatus(Book.ReadingStatus status) {
        // Cria um livro com cada status
        BookDTO dto = buildBookDTO("Livro Teste", "Autor", "isbn-" + status.name());
        dto.setStatus(status);
        bookService.create(dto, OWNER_ID);

        List<BookDTO> filtered = bookService.findByStatus(status, OWNER_ID);

        assertThat(filtered).hasSize(1);
        assertThat(filtered.get(0).getStatus()).isEqualTo(status);
    }

    // ========================
    // ATUALIZAÇÃO (UPDATE)
    // ========================

    @Test
    @DisplayName("Deve atualizar livro com sucesso")
    void shouldUpdateBookSuccessfully() {
        BookDTO created = bookService.create(
                buildBookDTO("Título Original", "Autor Original", "978-111"), OWNER_ID);

        BookDTO updateDTO = buildBookDTO("Título Atualizado", "Autor Novo", "978-111");
        updateDTO.setStatus(Book.ReadingStatus.LIDO);

        BookDTO updated = bookService.update(created.getId(), updateDTO, OWNER_ID);

        assertThat(updated.getTitle()).isEqualTo("Título Atualizado");
        assertThat(updated.getAuthor()).isEqualTo("Autor Novo");
        assertThat(updated.getStatus()).isEqualTo(Book.ReadingStatus.LIDO);
    }

    @Test
    @DisplayName("Deve lançar exceção ao atualizar livro de outro usuário")
    void shouldThrowExceptionWhenUpdatingAnotherUserBook() {
        BookDTO created = bookService.create(
                buildBookDTO("Livro", "Autor", "978-222"), OWNER_ID);

        BookDTO updateDTO = buildBookDTO("Hack", "Hacker", "978-222");

        assertThatThrownBy(() -> bookService.update(created.getId(), updateDTO, OTHER_OWNER_ID))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ========================
    // EXCLUSÃO (DELETE)
    // ========================

    @Test
    @DisplayName("Deve excluir livro com sucesso")
    void shouldDeleteBookSuccessfully() {
        BookDTO created = bookService.create(
                buildBookDTO("Para Deletar", "Autor", "978-333"), OWNER_ID);

        bookService.delete(created.getId(), OWNER_ID);

        // Verifica que o livro não existe mais no banco
        List<BookDTO> remaining = bookService.findAllByOwner(OWNER_ID);
        assertThat(remaining).isEmpty();
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar livro inexistente")
    void shouldThrowExceptionWhenDeletingNonExistentBook() {
        assertThatThrownBy(() -> bookService.delete("id-inexistente-xyz", OWNER_ID))
                .isInstanceOf(IllegalArgumentException.class);
    }

    // ========================
    // TESTES PARAMETRIZADOS — ISBNs inválidos/válidos
    // ========================

    @ParameterizedTest(name = "Deve aceitar título não-vazio: \"{0}\"")
    @ValueSource(strings = {"A", "AB", "Livro com Título Longo e Detalhado", "  Título com espaços  "})
    @DisplayName("Deve criar livro com diferentes formatos de título")
    void shouldCreateBookWithVariousTitleFormats(String title) {
        BookDTO dto = buildBookDTO(title.trim(), "Autor", "isbn-" + title.hashCode());
        BookDTO created = bookService.create(dto, OWNER_ID);
        assertThat(created.getTitle()).isEqualTo(title.trim());
    }

    // ========================
    // Método auxiliar — Builder de BookDTO
    // ========================

    private BookDTO buildBookDTO(String title, String author, String isbn) {
        return BookDTO.builder()
                .title(title)
                .author(author)
                .isbn(isbn)
                .genre("Tecnologia")
                .year(2023)
                .status(Book.ReadingStatus.QUERO_LER)
                .build();
    }
}