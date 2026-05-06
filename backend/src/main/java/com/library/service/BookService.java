package com.library.service;

import com.library.dto.BookDTO;
import com.library.model.Book;
import com.library.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Serviço de negócio para gerenciamento de Livros.
 *
 * A camada Service concentra TODA a lógica de negócio:
 * - Validações de regras (ISBN duplicado, dono do livro, etc.)
 * - Transformações de dados (DTO ↔ Entity)
 * - Chamadas ao repositório
 *
 * Os Controllers apenas delegam para cá — eles não têm lógica de negócio.
 */
@Service
@RequiredArgsConstructor  // Lombok: gera construtor com campos final (injeção via construtor)
public class BookService {

    private final BookRepository bookRepository;

    /**
     * Lista todos os livros de um usuário.
     *
     * @param ownerId ID do usuário autenticado
     * @return lista de DTOs dos livros
     */
    public List<BookDTO> findAllByOwner(String ownerId) {
        return bookRepository.findByOwnerId(ownerId)
                .stream()
                .map(BookDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Busca um livro pelo ID, verificando que pertence ao usuário.
     *
     * @throws IllegalArgumentException se o livro não for encontrado ou não pertencer ao usuário
     */
    public BookDTO findByIdAndOwner(String id, String ownerId) {
        return bookRepository.findByIdAndOwnerId(id, ownerId)
                .map(BookDTO::fromEntity)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado ou acesso negado"));
    }

    /**
     * Cadastra um novo livro.
     *
     * Regra de negócio: ISBN deve ser único por usuário.
     *
     * @throws IllegalArgumentException se o ISBN já existir para este usuário
     */
    public BookDTO create(BookDTO dto, String ownerId) {
        // Validação de regra de negócio: ISBN único por usuário
        if (bookRepository.existsByIsbnAndOwnerId(dto.getIsbn(), ownerId)) {
            throw new IllegalArgumentException("Já existe um livro com este ISBN na sua biblioteca");
        }

        Book book = dto.toEntity();
        book.setOwnerId(ownerId);
        book.setCreatedAt(LocalDateTime.now());
        book.setUpdatedAt(LocalDateTime.now());

        Book saved = bookRepository.save(book);
        return BookDTO.fromEntity(saved);
    }

    /**
     * Atualiza um livro existente.
     *
     * Verifica que o livro pertence ao usuário antes de atualizar.
     */
    public BookDTO update(String id, BookDTO dto, String ownerId) {
        Book existing = bookRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado ou acesso negado"));

        // Verifica ISBN duplicado apenas se o ISBN mudou
        if (!existing.getIsbn().equals(dto.getIsbn())
                && bookRepository.existsByIsbnAndOwnerId(dto.getIsbn(), ownerId)) {
            throw new IllegalArgumentException("Já existe um livro com este ISBN na sua biblioteca");
        }

        // Atualiza apenas os campos permitidos (preserva id, ownerId, createdAt)
        existing.setTitle(dto.getTitle());
        existing.setAuthor(dto.getAuthor());
        existing.setIsbn(dto.getIsbn());
        existing.setGenre(dto.getGenre());
        existing.setYear(dto.getYear());
        existing.setDescription(dto.getDescription());
        existing.setStatus(dto.getStatus());
        existing.setUpdatedAt(LocalDateTime.now());

        Book saved = bookRepository.save(existing);
        return BookDTO.fromEntity(saved);
    }

    /**
     * Remove um livro da biblioteca.
     *
     * Verifica que o livro pertence ao usuário antes de excluir.
     */
    public void delete(String id, String ownerId) {
        Book book = bookRepository.findByIdAndOwnerId(id, ownerId)
                .orElseThrow(() -> new IllegalArgumentException("Livro não encontrado ou acesso negado"));

        bookRepository.delete(book);
    }

    /**
     * Pesquisa livros pelo título.
     */
    public List<BookDTO> searchByTitle(String title, String ownerId) {
        return bookRepository.findByOwnerIdAndTitleContainingIgnoreCase(ownerId, title)
                .stream()
                .map(BookDTO::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * Filtra livros por status de leitura.
     */
    public List<BookDTO> findByStatus(Book.ReadingStatus status, String ownerId) {
        return bookRepository.findByOwnerIdAndStatus(ownerId, status)
                .stream()
                .map(BookDTO::fromEntity)
                .collect(Collectors.toList());
    }
}