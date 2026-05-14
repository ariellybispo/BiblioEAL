package com.library.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO com informações de livro retornadas pela API externa Open Library.
 * Usado exclusivamente para enriquecer dados via lookup por ISBN.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ExternalBookInfoDTO {

    /** Título do livro conforme a Open Library */
    private String title;

    /** Primeiro autor listado */
    private String author;

    /** Data/ano de publicação */
    private String publishDate;
}
