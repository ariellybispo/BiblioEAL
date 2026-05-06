package com.library.controller;

import com.library.dto.BookDTO;
import com.library.model.Book;
import com.library.model.User;
import com.library.service.BookService;
import com.library.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

/**
 * Controller para gerenciamento de Livros.
 *
 * Padrão MVC:
 * - @Controller: indica que esta classe retorna views (páginas HTML)
 * - Model: objeto usado para passar dados para o template Thymeleaf
 * - RedirectAttributes: passa mensagens entre redirecionamentos (flash messages)
 *
 * @AuthenticationPrincipal UserDetails: injeta automaticamente o usuário logado
 */
@Controller
@RequestMapping("/books")
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;
    private final UserService userService;

    /**
     * GET /books — Lista todos os livros do usuário.
     * Suporta filtro por título via query param ?search=
     */
    @GetMapping
    public String listBooks(
            @RequestParam(required = false) String search,
            @RequestParam(required = false) Book.ReadingStatus status,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model) {

        String ownerId = getOwnerId(userDetails);

        // Aplica filtro se houver parâmetro de busca
        var books = (search != null && !search.isBlank())
                ? bookService.searchByTitle(search, ownerId)
                : (status != null)
                    ? bookService.findByStatus(status, ownerId)
                    : bookService.findAllByOwner(ownerId);

        model.addAttribute("books", books);
        model.addAttribute("search", search);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("statuses", Book.ReadingStatus.values());
        model.addAttribute("username", userDetails.getUsername());

        return "books/list";  // → templates/books/list.html
    }

    /**
     * GET /books/new — Exibe formulário de cadastro de novo livro.
     */
    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("book", new BookDTO());
        model.addAttribute("statuses", Book.ReadingStatus.values());
        model.addAttribute("action", "Cadastrar");
        return "books/form";
    }

    /**
     * POST /books — Processa o cadastro de um novo livro.
     *
     * @Valid: ativa as validações definidas no DTO (Bean Validation)
     * BindingResult: captura erros de validação sem lançar exceção
     */
    @PostMapping
    public String createBook(
            @Valid @ModelAttribute("book") BookDTO bookDTO,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("statuses", Book.ReadingStatus.values());
            model.addAttribute("action", "Cadastrar");
            return "books/form";  // Volta ao formulário com os erros
        }

        try {
            String ownerId = getOwnerId(userDetails);
            bookService.create(bookDTO, ownerId);
            redirectAttributes.addFlashAttribute("success", "Livro cadastrado com sucesso!");
        } catch (IllegalArgumentException e) {
            // Erros de regra de negócio (ex: ISBN duplicado)
            model.addAttribute("error", e.getMessage());
            model.addAttribute("statuses", Book.ReadingStatus.values());
            model.addAttribute("action", "Cadastrar");
            return "books/form";
        }

        return "redirect:/books";
    }

    /**
     * GET /books/{id}/edit — Exibe formulário de edição.
     */
    @GetMapping("/{id}/edit")
    public String showEditForm(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            RedirectAttributes redirectAttributes) {

        try {
            String ownerId = getOwnerId(userDetails);
            BookDTO book = bookService.findByIdAndOwner(id, ownerId);
            model.addAttribute("book", book);
            model.addAttribute("statuses", Book.ReadingStatus.values());
            model.addAttribute("action", "Atualizar");
            return "books/form";
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
            return "redirect:/books";
        }
    }

    /**
     * POST /books/{id} — Processa a atualização de um livro.
     * (HTML forms não suportam PUT/PATCH nativamente, usamos POST)
     */
    @PostMapping("/{id}")
    public String updateBook(
            @PathVariable String id,
            @Valid @ModelAttribute("book") BookDTO bookDTO,
            BindingResult result,
            @AuthenticationPrincipal UserDetails userDetails,
            Model model,
            RedirectAttributes redirectAttributes) {

        if (result.hasErrors()) {
            model.addAttribute("statuses", Book.ReadingStatus.values());
            model.addAttribute("action", "Atualizar");
            return "books/form";
        }

        try {
            String ownerId = getOwnerId(userDetails);
            bookService.update(id, bookDTO, ownerId);
            redirectAttributes.addFlashAttribute("success", "Livro atualizado com sucesso!");
        } catch (IllegalArgumentException e) {
            model.addAttribute("error", e.getMessage());
            model.addAttribute("statuses", Book.ReadingStatus.values());
            model.addAttribute("action", "Atualizar");
            return "books/form";
        }

        return "redirect:/books";
    }

    /**
     * POST /books/{id}/delete — Remove um livro.
     */
    @PostMapping("/{id}/delete")
    public String deleteBook(
            @PathVariable String id,
            @AuthenticationPrincipal UserDetails userDetails,
            RedirectAttributes redirectAttributes) {

        try {
            String ownerId = getOwnerId(userDetails);
            bookService.delete(id, ownerId);
            redirectAttributes.addFlashAttribute("success", "Livro removido com sucesso!");
        } catch (IllegalArgumentException e) {
            redirectAttributes.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/books";
    }

    /**
     * Método auxiliar: obtém o ID do usuário autenticado via UserService.
     * O Spring Security nos fornece o username, mas precisamos do ID do MongoDB.
     */
    private String getOwnerId(UserDetails userDetails) {
        User user = userService.findByUsername(userDetails.getUsername());
        return user.getId();
    }
}