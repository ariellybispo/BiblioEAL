package com.library.dto;

import com.library.model.Book;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO (Data Transfer Object) para receber e enviar dados de Livro.
 *
 * Por que usar DTO e não a entidade diretamente?
 * - Separa a camada de apresentação da camada de persistência
 * - Permite validações específicas para formulários
 * - Evita expor campos internos (como ownerId) ao frontend
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookDTO {

    private String id;

    @NotBlank(message = "Título é obrigatório")
    private String title;

    @NotBlank(message = "Autor é obrigatório")
    private String author;

    @NotBlank(message = "ISBN é obrigatório")
    private String isbn;

    private String genre;

    @Min(value = 1000, message = "Ano inválido")
    @Max(value = 2100, message = "Ano inválido")
    private Integer year;

    private String description;

    private Book.ReadingStatus status;

    /**
     * Converte este DTO para a entidade Book.
     * O ownerId e as datas são preenchidos pelo Service, não pelo formulário.
     */
    public Book toEntity() {
        return Book.builder()
                .id(this.id)
                .title(this.title)
                .author(this.author)
                .isbn(this.isbn)
                .genre(this.genre)
                .year(this.year)
                .description(this.description)
                .status(this.status != null ? this.status : Book.ReadingStatus.QUERO_LER)
                .build();
    }

    /**
     * Cria um DTO a partir de uma entidade Book.
     */
    public static BookDTO fromEntity(Book book) {
        return BookDTO.builder()
                .id(book.getId())
                .title(book.getTitle())
                .author(book.getAuthor())
                .isbn(book.getIsbn())
                .genre(book.getGenre())
                .year(book.getYear())
                .description(book.getDescription())
                .status(book.getStatus())
                .build();
    }
}