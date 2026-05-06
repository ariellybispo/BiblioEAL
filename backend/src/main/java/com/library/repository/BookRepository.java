package com.library.repository;

import com.library.model.Book;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

/**
 * Repositório para operações de banco de dados de Livros.
 *
 * O Spring Data MongoDB gera a implementação automaticamente
 * com base nos nomes dos métodos (Query DSL).
 *
 * Não precisamos escrever queries — o Spring interpreta
 * findByOwnerId como: db.books.find({ ownerId: ? })
 */
@Repository
public interface BookRepository extends MongoRepository<Book, String> {

    /**
     * Lista todos os livros de um usuário específico.
     * Query gerada: { ownerId: ownerId }
     */
    List<Book> findByOwnerId(String ownerId);

    /**
     * Busca livro pelo ID e ownerId (segurança: garante que pertence ao usuário).
     * Query gerada: { _id: id, ownerId: ownerId }
     */
    Optional<Book> findByIdAndOwnerId(String id, String ownerId);

    /**
     * Verifica se existe livro com o ISBN para o usuário (validação de duplicata).
     * Query gerada: { isbn: isbn, ownerId: ownerId }
     */
    boolean existsByIsbnAndOwnerId(String isbn, String ownerId);

    /**
     * Busca livros cujo título contenha a string informada (case-insensitive).
     * Query gerada: { ownerId: ownerId, title: { $regex: title, $options: 'i' } }
     */
    List<Book> findByOwnerIdAndTitleContainingIgnoreCase(String ownerId, String title);

    /**
     * Filtra livros pelo status de leitura do usuário.
     * Query gerada: { ownerId: ownerId, status: status }
     */
    List<Book> findByOwnerIdAndStatus(String ownerId, Book.ReadingStatus status);
}
