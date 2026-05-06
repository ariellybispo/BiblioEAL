package com.library.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

/**
 * Entidade Book — representa um livro na biblioteca pessoal do usuário.
 *
 * @Document: mapeia esta classe para a coleção "books" no MongoDB.
 * O MongoDB não usa tabelas (como SQL), mas sim coleções de documentos JSON.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {

    @Id
    private String id;

    /** Título do livro */
    private String title;

    /** Autor do livro */
    private String author;

    /**
     * ISBN (International Standard Book Number).
     * Regra de negócio: único por usuário (não globalmente).
     */
    private String isbn;

    /** Gênero literário (opcional) */
    private String genre;

    /** Ano de publicação (opcional) */
    private Integer year;

    /** Notas pessoais / descrição do usuário (opcional) */
    private String description;

    /** Status de leitura (obrigatório, padrão: QUERO_LER) */
    private ReadingStatus status;

    /**
     * ID do usuário dono do livro.
     * Não é exposto no DTO — segurança contra manipulação.
     */
    private String ownerId;

    /** Data de cadastro (preenchida automaticamente pelo Service) */
    private LocalDateTime createdAt;

    /** Data da última atualização */
    private LocalDateTime updatedAt;

    /**
     * Status de leitura do livro.
     * Cada valor tem um label amigável para exibição no frontend.
     */
    public enum ReadingStatus {
        QUERO_LER("Quero Ler"),
        LENDO("Lendo"),
        LIDO("Lido");

        private final String label;

        ReadingStatus(String label) {
            this.label = label;
        }

        public String getLabel() {
            return label;
        }
    }
}
