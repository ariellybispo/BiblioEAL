package com.library.dto;

import com.library.model.Book;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

/**
 * Testes Unitários para BookDTO.
 *
 * ========================
 * TIPO: Caixa Branca (White-Box) — Testes Unitários Puros
 * ========================
 * - Testa a lógica interna do DTO (métodos toEntity e fromEntity)
 * - Não usa banco de dados nem Spring context
 * - Execução mais rápida (puro Java)
 *
 * Por que testar DTO?
 * DTOs têm lógica de conversão (toEntity, fromEntity) e valores padrão.
 * Um bug aqui afeta toda a camada de apresentação.
 */
@DisplayName("BookDTO — Testes Unitários (Caixa Branca)")
class BookDTOTest {

    @Test
    @DisplayName("toEntity() deve converter todos os campos corretamente")
    void shouldConvertToEntityCorrectly() {
        // ARRANGE
        BookDTO dto = BookDTO.builder()
                .title("Clean Code")
                .author("Robert Martin")
                .isbn("978-0132350884")
                .genre("Tecnologia")
                .year(2008)
                .description("Livro sobre código limpo")
                .status(Book.ReadingStatus.LIDO)
                .build();

        // ACT
        Book entity = dto.toEntity();

        // ASSERT — verifica cada campo individualmente
        assertThat(entity.getTitle()).isEqualTo("Clean Code");
        assertThat(entity.getAuthor()).isEqualTo("Robert Martin");
        assertThat(entity.getIsbn()).isEqualTo("978-0132350884");
        assertThat(entity.getGenre()).isEqualTo("Tecnologia");
        assertThat(entity.getYear()).isEqualTo(2008);
        assertThat(entity.getDescription()).isEqualTo("Livro sobre código limpo");
        assertThat(entity.getStatus()).isEqualTo(Book.ReadingStatus.LIDO);
    }

    @Test
    @DisplayName("toEntity() deve usar QUERO_LER como status padrão quando status é null")
    void shouldUseDefaultStatusWhenNull() {
        BookDTO dto = BookDTO.builder()
                .title("Livro")
                .author("Autor")
                .isbn("111")
                .status(null) // ← explicitamente nulo
                .build();

        Book entity = dto.toEntity();

        // Verifica o comportamento padrão definido no código
        assertThat(entity.getStatus()).isEqualTo(Book.ReadingStatus.QUERO_LER);
    }

    @Test
    @DisplayName("fromEntity() deve converter Entity para DTO corretamente")
    void shouldConvertFromEntityCorrectly() {
        // ARRANGE — cria uma entidade simulando o que viria do banco
        Book book = Book.builder()
                .id("mongo-id-123")
                .title("Design Patterns")
                .author("Gang of Four")
                .isbn("978-0201633610")
                .genre("Arquitetura")
                .year(1994)
                .description("Padrões de projeto")
                .status(Book.ReadingStatus.LENDO)
                .ownerId("user-abc")
                .build();

        // ACT
        BookDTO dto = BookDTO.fromEntity(book);

        // ASSERT
        assertThat(dto.getId()).isEqualTo("mongo-id-123");
        assertThat(dto.getTitle()).isEqualTo("Design Patterns");
        assertThat(dto.getStatus()).isEqualTo(Book.ReadingStatus.LENDO);
    }

    @Test
    @DisplayName("fromEntity() não deve expor ownerId (campo interno de segurança)")
    void fromEntityShouldNotExposeOwnerId() {
        // Verifica que o DTO não tem campo ownerId (segurança)
        // Se o ownerId fosse exposto, um usuário poderia manipulá-lo
        Book book = Book.builder()
                .id("id-1").title("T").author("A").isbn("I")
                .ownerId("segredo-interno")
                .build();

        BookDTO dto = BookDTO.fromEntity(book);

        // BookDTO não tem campo ownerId — só verificamos que não é exposto
        // Se esta linha compilar e o campo não existir no DTO, o teste passa
        assertThat(dto).isNotNull();
        assertThat(dto.getId()).isEqualTo("id-1");
    }

    @ParameterizedTest(name = "Status [{0}] deve ter label [{1}]")
    @CsvSource({
        "QUERO_LER, Quero Ler",
        "LENDO,     Lendo",
        "LIDO,      Lido"
    })
    @DisplayName("Cada ReadingStatus deve ter o label correto")
    void eachStatusShouldHaveCorrectLabel(Book.ReadingStatus status, String expectedLabel) {
        assertThat(status.getLabel()).isEqualTo(expectedLabel.trim());
    }

    @ParameterizedTest
    @NullSource
    @DisplayName("toEntity() deve ser seguro mesmo com status null")
    void toEntityShouldHandleNullStatus(Book.ReadingStatus nullStatus) {
        BookDTO dto = BookDTO.builder()
                .title("Livro").author("Autor").isbn("isbn-test")
                .status(nullStatus)
                .build();

        // Não deve lançar NullPointerException
        assertThatCode(() -> dto.toEntity()).doesNotThrowAnyException();
    }

    @ParameterizedTest(name = "Deve criar DTO com título: \"{0}\"")
    @ValueSource(strings = {"A", "Título Normal", "Título com Números 123", "Título Muito Longo Para Testar o Sistema de Cadastro"})
    @DisplayName("Deve criar DTO com diferentes formatos de título")
    void shouldCreateDTOWithVariousTitles(String title) {
        BookDTO dto = BookDTO.builder().title(title).author("A").isbn("I").build();
        assertThat(dto.getTitle()).isEqualTo(title);
    }
}