# 📚 BiblioEAL — Personal Library Manager

Sistema completo para gerenciamento de biblioteca pessoal com autenticação de usuários.

---

## 🏗️ Estrutura do Projeto

```
BiblioEAL/
├── backend/                          ← Aplicação Spring Boot
│   ├── pom.xml
│   └── src/
│       ├── main/java/com/library/
│       │   ├── PersonalLibraryApplication.java
│       │   ├── config/
│       │   │   └── SecurityConfig.java        ← Spring Security
│       │   ├── controller/
│       │   │   ├── AuthController.java         ← Login / Cadastro
│       │   │   └── BookController.java         ← CRUD de Livros
│       │   ├── service/
│       │   │   ├── UserService.java            ← Lógica de Usuários
│       │   │   └── BookService.java            ← Lógica de Livros
│       │   ├── repository/
│       │   │   ├── UserRepository.java         ← MongoDB Users
│       │   │   └── BookRepository.java         ← MongoDB Books
│       │   ├── model/
│       │   │   ├── User.java                   ← Entidade Usuário
│       │   │   └── Book.java                   ← Entidade Livro
│       │   └── dto/
│       │       ├── UserRegistrationDTO.java     ← DTO de Cadastro
│       │       └── BookDTO.java                 ← DTO de Livro
│       ├── main/resources/
│       │   ├── application.properties
│       │   ├── templates/
│       │   │   ├── auth/login.html             ← Página de Login
│       │   │   ├── auth/register.html          ← Página de Cadastro
│       │   │   ├── books/list.html             ← Listagem de Livros
│       │   │   └── books/form.html             ← Formulário Cadastro/Edição
│       │   └── static/
│       │       ├── css/style.css
│       │       └── js/app.js
│       └── test/java/com/library/
│           ├── BaseIntegrationTest.java         ← Base Testcontainers
│           ├── controller/
│           │   ├── AuthControllerE2ETest.java   ← E2E Auth
│           │   └── BookControllerE2ETest.java   ← E2E Books
│           ├── service/
│           │   ├── UserServiceIntegrationTest.java
│           │   └── BookServiceIntegrationTest.java
│           └── dto/
│               ├── UserRegistrationDTOTest.java ← Unitário
│               └── BookDTOTest.java             ← Unitário
├── .github/workflows/ci.yml          ← GitHub Actions CI
├── docker-compose.yml                ← App + MongoDB + SonarQube
├── Dockerfile                        ← Multi-stage build
└── RTM.md                            ← Matriz de Rastreabilidade
```

---

## ⚙️ Tecnologias

| Tecnologia           | Versão  | Função                              |
|----------------------|---------|-------------------------------------|
| Java                 | 17      | Linguagem                           |
| Spring Boot          | 3.2.5   | Framework principal                 |
| Spring Security      | 6.x     | Autenticação e autorização          |
| Spring Data MongoDB  | 4.x     | Persistência NoSQL                  |
| Thymeleaf            | 3.x     | Template engine (views HTML)        |
| MongoDB              | 6.0     | Banco de dados                      |
| Testcontainers       | 1.19.7  | Testes com banco real em Docker     |
| JaCoCo               | 0.8.11  | Cobertura de código (≥ 80%)         |
| SonarQube            | 10.x    | Análise estática de qualidade       |
| Docker / Compose     | —       | Containerização                     |

---

## 🚀 Como Executar

### Pré-requisitos
- Java 17+
- Maven 3.8+
- Docker + Docker Compose

### 1. Subir com Docker Compose (recomendado)

```bash
# Sobe MongoDB + Aplicação + SonarQube
docker-compose up -d

# Aguarda a aplicação iniciar (~60 segundos)
docker-compose logs -f app
```

Acesse em: **http://localhost:8080**

### 2. Executar localmente (desenvolvimento)

```bash
# Subir apenas o MongoDB
docker-compose up -d mongodb

# Executar a aplicação
cd backend
mvn spring-boot:run
```

---

## 🧪 Testes

### Executar todos os testes + relatório de cobertura

```bash
cd backend
mvn verify
```

O relatório JaCoCo será gerado em:
`backend/target/site/jacoco/index.html`

### Executar apenas testes unitários (sem Docker)

```bash
mvn test -pl backend -Dtest="BookDTOTest,UserRegistrationDTOTest"
```

### Executar testes de integração (requer Docker)

```bash
mvn verify -pl backend
```

---

## 🔍 SonarQube

### Subir o SonarQube local

```bash
docker-compose up -d sonarqube sonar-postgres
# Aguarde ~2 minutos, acesse http://localhost:9000
# Login: admin / admin
```

### Criar projeto e token

1. Acesse http://localhost:9000
2. Crie um projeto manual com key: `personal-library`
3. Gere um token de análise
4. Configure no GitHub Secrets: `SONAR_TOKEN` e `SONAR_HOST_URL`

### Rodar análise manualmente

```bash
cd backend
mvn verify sonar:sonar \
  -Dsonar.projectKey=personal-library \
  -Dsonar.host.url=http://localhost:9000 \
  -Dsonar.login=SEU_TOKEN
```

---

## 🏛️ Arquitetura MVC

```
Request HTTP
    │
    ▼
┌─────────────────┐
│   Controller    │  ← Recebe requisições, valida, delega
│  (AuthController│
│  BookController)│
└────────┬────────┘
         │ chama
         ▼
┌─────────────────┐
│    Service      │  ← Lógica de negócio, regras, transformações
│  (UserService   │
│   BookService)  │
└────────┬────────┘
         │ chama
         ▼
┌─────────────────┐
│   Repository    │  ← Acesso ao MongoDB (Spring Data)
│ (UserRepository │
│  BookRepository)│
└────────┬────────┘
         │
         ▼
┌─────────────────┐
│    MongoDB      │  ← Persistência (coleções: users, books)
└─────────────────┘
```

---

## 🛡️ Estratégia de Testes

```
           /\
          /  \          E2E / Controller
         /────\         AuthControllerE2ETest, BookControllerE2ETest
        /      \        MockMvc + Testcontainers
       /────────\       
      /          \      Integração
     /────────────\     UserServiceIntegrationTest, BookServiceIntegrationTest
    /              \    Testcontainers (MongoDB real)
   /────────────────\   
  /                  \  Unitários
 /────────────────────\ BookDTOTest, UserRegistrationDTOTest
                        JUnit 5 puro (sem Spring, sem banco)
```

### Por que Testcontainers e não Mocks?

| Critério | Mock | Testcontainers |
|---|---|---|
| Fidelidade ao MongoDB | ⚠️ Simula | ✅ Real |
| Índices únicos respeitados | ⚠️ Não | ✅ Sim |
| Velocidade | ✅ Rápido | ⚠️ Mais lento |
| Confiabilidade | ⚠️ Pode mascarar bugs | ✅ Revela bugs reais |

---

## 📋 Funcionalidades

- **Cadastro de Usuários** — nome, username único, email único, senha com hash BCrypt
- **Login / Logout** — sessão HTTP gerenciada pelo Spring Security
- **CRUD de Livros** — criar, listar, editar, excluir
- **Filtros** — busca por título (case-insensitive), filtro por status de leitura
- **Isolamento de dados** — cada usuário vê apenas seus próprios livros
- **Validação** — Bean Validation nos formulários
- **Proteção CSRF** — tokens em todos os formulários POST

---

## 📁 RTM

Ver o arquivo [RTM.md](RTM.md) para a Matriz de Rastreabilidade de Requisitos completa com diagramas UML de sequência.

Alan Fonseca
Arielly bispo
Eduardo Sampaio
