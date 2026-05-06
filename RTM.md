# RTM — Matriz de Rastreabilidade de Requisitos
## Personal Library Manager

> **Definição:** A RTM (Requirements Traceability Matrix) mapeia cada requisito funcional do sistema ao(s) teste(s) que o validam, garantindo cobertura completa. Nenhum requisito existe sem teste; nenhum teste existe sem requisito.

---

## Índice de Requisitos

| ID  | Requisito                          | Tipo          | Testes Vinculados                                   | Status |
|-----|------------------------------------|---------------|-----------------------------------------------------|--------|
| RF01 | Cadastro de Usuário               | Funcional     | `shouldRegisterUserSuccessfully`                    | ✅     |
| RF02 | Senha com hash BCrypt             | Segurança     | `shouldStorePasswordAsHash`                         | ✅     |
| RF03 | Username e email únicos           | Funcional     | `shouldThrowExceptionOnDuplicateUsername/Email`     | ✅     |
| RF04 | Confirmação de senha              | Funcional     | `shouldThrowExceptionWhenPasswordsDontMatch`        | ✅     |
| RF05 | Autenticação de usuário           | Funcional     | `shouldLoadUserByUsernameForAuthentication`         | ✅     |
| RF06 | Isolamento de dados por usuário   | Segurança     | `shouldListOnlyOwnerBooks`                          | ✅     |
| RF07 | Cadastro de livro (CREATE)        | Funcional     | `shouldCreateBookSuccessfully`, `shouldCreateBookAndRedirect` | ✅ |
| RF08 | ISBN único por usuário            | Regra negócio | `shouldThrowExceptionOnDuplicateIsbnSameOwner`      | ✅     |
| RF09 | Listagem de livros (READ)         | Funcional     | `shouldReturn200WithBookList`                       | ✅     |
| RF10 | Busca por título                  | Funcional     | `shouldSearchBooksIgnoringCase`                     | ✅     |
| RF11 | Filtro por status de leitura      | Funcional     | `shouldFilterByEachReadingStatus` (parametrizado)   | ✅     |
| RF12 | Edição de livro (UPDATE)          | Funcional     | `shouldUpdateBookSuccessfully`                      | ✅     |
| RF13 | Exclusão de livro (DELETE)        | Funcional     | `shouldDeleteBookSuccessfully`, `shouldDeleteBookAndRedirect` | ✅ |
| RF14 | Proteção de rotas (autenticação)  | Segurança     | `shouldRedirectToLoginWhenNotAuthenticated`         | ✅     |
| RF15 | Sessão gerenciada (login/logout)  | Funcional     | SecurityConfig + `shouldShowLoginPage`              | ✅     |
| RF16 | Validação de formulários          | Funcional     | `shouldReturnFormWithErrorsWhenTitleEmpty`          | ✅     |

---

## RF01 — Cadastro de Usuário

**Descrição:** O sistema deve permitir que novos usuários se cadastrem informando nome, username, email e senha.

**Teste(s):** `UserServiceIntegrationTest::shouldRegisterUserSuccessfully`

```
Diagrama UML de Sequência — RF01: Cadastro de Usuário

Usuário          Browser         AuthController     UserService       MongoDB
   |                |                  |                 |               |
   |-- preenche --->|                  |                 |               |
   |   formulário   |                  |                 |               |
   |                |-- POST /register |                 |               |
   |                |   {name, user,   |                 |               |
   |                |    email, pass}  |                 |               |
   |                |                  |                 |               |
   |                |                  |-- register(dto) |               |
   |                |                  |                 |               |
   |                |                  |                 |-- existsByUsername()
   |                |                  |                 |               |
   |                |                  |                 |<-- false ------|
   |                |                  |                 |               |
   |                |                  |                 |-- existsByEmail()
   |                |                  |                 |               |
   |                |                  |                 |<-- false ------|
   |                |                  |                 |               |
   |                |                  |                 |-- BCrypt.hash(password)
   |                |                  |                 |               |
   |                |                  |                 |-- save(user) ->|
   |                |                  |                 |               |
   |                |                  |                 |<-- User salvo -|
   |                |                  |<-- User --------|               |
   |                |<-- redirect -----|                 |               |
   |                |    /login        |                 |               |
   |<-- Página -----|                  |                 |               |
   |   Login        |                  |                 |               |
```

---

## RF02 — Senha com Hash BCrypt

**Descrição:** Senhas nunca devem ser armazenadas em texto puro. O sistema deve usar BCrypt para hash.

**Teste(s):** `UserServiceIntegrationTest::shouldStorePasswordAsHash`

**Por que BCrypt?**
- É lento propositalmente (custo configurável), dificultando ataques de força bruta
- Inclui "salt" automático — duas senhas iguais geram hashes diferentes
- Padrão ouro da indústria para senhas em aplicações web

```
Diagrama UML de Sequência — RF02: Hash de Senha

UserService          BCryptPasswordEncoder         MongoDB
    |                         |                       |
    |-- encode("senha123") -->|                       |
    |                         |                       |
    |                         |-- gera salt aleatório |
    |                         |-- aplica 2^10 rounds  |
    |                         |-- produz hash         |
    |                         |                       |
    |<-- "$2a$10$xyz..." ------|                       |
    |                         |                       |
    |-- save(user com hash) ---------------------------->|
    |                         |                       |
    |                         |                       |
    [Verificação no login]     |                       |
    |                         |                       |
    |-- matches("senha123", "$2a$10$xyz...")           |
    |                    ---->|                       |
    |<-- true ----------------|                       |
```

---

## RF05 — Autenticação de Usuário

**Descrição:** Usuários cadastrados devem conseguir fazer login com username e senha.

**Teste(s):** `UserServiceIntegrationTest::shouldLoadUserByUsernameForAuthentication`

```
Diagrama UML de Sequência — RF05: Autenticação (Login)

Usuário       Browser      Spring Security      UserService       MongoDB
   |              |               |                  |               |
   |-- digita --->|               |                  |               |
   |  user/senha  |               |                  |               |
   |              |-- POST /login |                  |               |
   |              |               |                  |               |
   |              |               |-- loadUserByUsername("user")     |
   |              |               |                  |               |
   |              |               |                  |-- findByUsername()
   |              |               |                  |               |
   |              |               |                  |<-- User -------|
   |              |               |<-- UserDetails --|               |
   |              |               |                  |               |
   |              |               |-- BCrypt.matches(senha, hash)    |
   |              |               |                  |               |
   |              |               |-- [OK] cria Sessão HTTP          |
   |              |               |-- Salva JSESSIONID no Cookie     |
   |              |               |                  |               |
   |              |<-- redirect --|                  |               |
   |              |   /books      |                  |               |
   |<-- Página----|               |                  |               |
   |   /books     |               |                  |               |
```

---

## RF06 — Isolamento de Dados por Usuário

**Descrição:** Cada usuário só pode visualizar, editar e excluir os seus próprios livros.

**Teste(s):** `BookServiceIntegrationTest::shouldListOnlyOwnerBooks`, `shouldThrowExceptionWhenAccessingAnotherUserBook`

```
Diagrama UML de Sequência — RF06: Isolamento de Dados

UsuárioA       BookController       BookService        MongoDB
   |                  |                  |                |
   |-- GET /books --->|                  |                |
   |                  |                  |                |
   |                  |-- findAllByOwner(userA.id)        |
   |                  |                  |                |
   |                  |                  |-- db.books.find({ ownerId: "userA" })
   |                  |                  |                |
   |                  |                  |<-- [Livros A] -|
   |                  |<-- [Livros A] ---|                |
   |<-- Página -------|                  |                |
   |   (só livros A)  |                  |                |
   |                  |                  |                |
   [UsuárioA tenta acessar livro do B via URL]            |
   |                  |                  |                |
   |-- GET /books/id-livro-B/edit ------>|                |
   |                  |                  |                |
   |                  |-- findByIdAndOwnerId(id, userA.id)|
   |                  |                  |                |
   |                  |                  |-- db.books.find({ _id: "id-livro-B", ownerId: "userA" })
   |                  |                  |                |
   |                  |                  |<-- null -------|  (não encontrou)
   |                  |                  |                |
   |                  |                  |-- throw IllegalArgumentException
   |                  |<-- redirect -----|                |
   |<-- /books -------|  (acesso negado) |                |
```

---

## RF07 — Cadastro de Livro (CREATE)

**Descrição:** Usuário autenticado pode cadastrar um livro informando título, autor e ISBN.

**Teste(s):** `BookServiceIntegrationTest::shouldCreateBookSuccessfully`, `BookControllerE2ETest::shouldCreateBookAndRedirect`

```
Diagrama UML de Sequência — RF07: Cadastro de Livro

Usuário       Browser       BookController      BookService        MongoDB
   |              |                |                  |               |
   |-- preenche ->|                |                  |               |
   |  formulário  |                |                  |               |
   |              |-- POST /books->|                  |               |
   |              |   {title,      |                  |               |
   |              |    author,isbn}|                  |               |
   |              |                |                  |               |
   |              |                |-- @Valid valida DTO              |
   |              |                |                  |               |
   |              |                |-- create(dto, ownerId)           |
   |              |                |                  |               |
   |              |                |                  |-- existsByIsbnAndOwnerId()
   |              |                |                  |               |
   |              |                |                  |<-- false ------|
   |              |                |                  |               |
   |              |                |                  |-- setOwnerId()
   |              |                |                  |-- setCreatedAt()
   |              |                |                  |-- save(book) ->|
   |              |                |                  |               |
   |              |                |                  |<-- Book -------|
   |              |                |<-- BookDTO ------|               |
   |              |<-- redirect ---|                  |               |
   |              |   /books       |                  |               |
   |<-- Lista -----|               |                  |               |
   |   atualizada  |               |                  |               |
```

---

## RF08 — ISBN Único por Usuário

**Descrição:** Não é permitido cadastrar dois livros com o mesmo ISBN na biblioteca do mesmo usuário. ISBNs iguais entre usuários diferentes são permitidos.

**Teste(s):** `shouldThrowExceptionOnDuplicateIsbnSameOwner`, `shouldAllowSameIsbnForDifferentOwners`

```
Diagrama UML de Sequência — RF08: Validação de ISBN Duplicado

BookService                    MongoDB
    |                             |
    |-- existsByIsbnAndOwnerId("978-...", "userA")
    |                             |
    |                             |-- db.books.find({ isbn: "978-...", ownerId: "userA" })
    |                             |
    |  [Livro encontrado]         |
    |<-- true -------------------|
    |                             |
    |-- throw IllegalArgumentException("Já existe um livro com este ISBN")
    |
    | [Fluxo alternativo: ISBN do mesmo livro mas outro usuário]
    |
    |-- existsByIsbnAndOwnerId("978-...", "userB")
    |                             |
    |                             |-- db.books.find({ isbn: "978-...", ownerId: "userB" })
    |                             |
    |<-- false ------------------|  (userB não tem este livro)
    |                             |
    |-- save(book) -------------->|  (permite cadastrar)
```

---

## RF12 — Edição de Livro (UPDATE)

**Descrição:** Usuário pode editar informações de um livro existente em sua biblioteca.

**Teste(s):** `shouldUpdateBookSuccessfully`, `shouldThrowExceptionWhenUpdatingAnotherUserBook`

```
Diagrama UML de Sequência — RF12: Atualização de Livro

Usuário       Browser       BookController      BookService        MongoDB
   |              |                |                  |               |
   |              |-- POST /books/{id}               |               |
   |              |   {campos atualizados}           |               |
   |              |                |                  |               |
   |              |                |-- update(id, dto, ownerId)       |
   |              |                |                  |               |
   |              |                |                  |-- findByIdAndOwnerId(id, ownerId)
   |              |                |                  |               |
   |              |                |                  |<-- Book ------|
   |              |                |                  |               |
   |              |                |                  |-- valida ISBN duplicado
   |              |                |                  |-- atualiza campos
   |              |                |                  |-- setUpdatedAt()
   |              |                |                  |-- save(book) ->|
   |              |                |                  |               |
   |              |<-- redirect ---|                  |               |
   |<-- /books ---|                |                  |               |
```

---

## RF13 — Exclusão de Livro (DELETE)

**Descrição:** Usuário pode remover um livro de sua biblioteca.

**Teste(s):** `shouldDeleteBookSuccessfully`, `shouldDeleteBookAndRedirect`

```
Diagrama UML de Sequência — RF13: Exclusão de Livro

Usuário       Browser       BookController      BookService        MongoDB
   |              |                |                  |               |
   |-- confirma ->|                |                  |               |
   |   exclusão   |                |                  |               |
   |              |-- POST /books/{id}/delete         |               |
   |              |   + CSRF token |                  |               |
   |              |                |                  |               |
   |              |                |-- delete(id, ownerId)            |
   |              |                |                  |               |
   |              |                |                  |-- findByIdAndOwnerId()
   |              |                |                  |               |
   |              |                |                  |<-- Book ------|
   |              |                |                  |               |
   |              |                |                  |-- delete(book)->|
   |              |                |                  |               |
   |              |<-- redirect ---|                  |               |
   |<-- /books ---|                |                  |               |
   |   (removido) |                |                  |               |
```

---

## RF14 — Proteção de Rotas

**Descrição:** Rotas que exigem autenticação devem redirecionar para login quando acessadas sem sessão ativa.

**Teste(s):** `BookControllerE2ETest::shouldRedirectToLoginWhenNotAuthenticated`

```
Diagrama UML de Sequência — RF14: Proteção de Rota

Usuário não      Browser       Spring Security        Servidor
autenticado         |               |                     |
   |                |               |                     |
   |-- acessa ----->|               |                     |
   |   /books       |               |                     |
   |                |-- GET /books->|                     |
   |                |               |                     |
   |                |               |-- verifica Sessão   |
   |                |               |   (JSESSIONID)      |
   |                |               |                     |
   |                |               |-- Sessão inválida!  |
   |                |               |                     |
   |                |<-- 302 -------|                     |
   |                |   redirect    |                     |
   |                |   /login      |                     |
   |<-- Página -----|               |                     |
   |   Login        |               |                     |
```

---

## RF15 — Gerenciamento de Sessão

**Descrição:** O sistema deve manter sessão autenticada e permitir logout seguro.

```
Diagrama UML de Sequência — RF15: Logout

Usuário       Browser       Spring Security        Servidor
   |              |               |                    |
   |-- clica ---->|               |                    |
   |   "Sair"     |               |                    |
   |              |-- POST /logout + CSRF token        |
   |              |               |                    |
   |              |               |-- invalida Sessão  |
   |              |               |-- apaga JSESSIONID |
   |              |               |   cookie           |
   |              |               |                    |
   |              |<-- redirect --|                    |
   |              |   /login      |                    |
   |<-- Página----|               |                    |
   |   Login      |               |                    |
   |   "Você saiu"|               |                    |
```

---

## Estratégia de Testes

### Pirâmide de Testes Adotada

```
           /\
          /  \          ← E2E / Controller (BookControllerE2ETest)
         /────\            Testa fluxo completo HTTP
        /      \
       /────────\       ← Integração (BookService, UserService IntegrationTest)
      /          \         Testa Service + MongoDB real (Testcontainers)
     /────────────\
    /              \    ← Unitários (BookDTOTest, UserRegistrationDTOTest)
   /────────────────\      Testa lógica pura sem infraestrutura
```

### Por que Testcontainers em vez de Mocks?

| Aspecto         | Mock (H2 embutido)          | Testcontainers (MongoDB real)     |
|-----------------|-----------------------------|-----------------------------------|
| Fidelidade      | ⚠️ Simula comportamento      | ✅ Comportamento idêntico à produção |
| Índices únicos  | ⚠️ Pode não respeitar        | ✅ Respeitados (como em produção)  |
| Queries complexas | ⚠️ Podem não funcionar     | ✅ Executam como em produção       |
| Velocidade      | ✅ Mais rápido               | ⚠️ Mais lento (inicia container)  |
| Confiabilidade  | ⚠️ Pode mascarar bugs        | ✅ Revela bugs reais               |

**Conclusão:** Testcontainers garante que o que funciona nos testes, funciona em produção.

---

*Documento gerado para o projeto Personal Library Manager — Foco em QA*