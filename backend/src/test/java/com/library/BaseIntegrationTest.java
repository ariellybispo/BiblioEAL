package com.library;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;

/**
 * Classe base para todos os testes de integração.
 *
 * ========================
 * POR QUE TESTCONTAINERS?
 * ========================
 * Mocks simulam comportamentos — Testcontainers usa um banco REAL em Docker.
 * Isso garante que os testes refletem exatamente o comportamento em produção:
 * - Queries reais são executadas
 * - Índices únicos do MongoDB são respeitados
 * - Erros reais de banco surgem nos testes
 *
 * ========================
 * COMO FUNCIONA?
 * ========================
 * 1. @Testcontainers: habilita o suporte JUnit 5 ao Testcontainers
 * 2. @Container: gerencia o ciclo de vida do container (start/stop)
 * 3. @DynamicPropertySource: sobrescreve as propriedades do Spring
 *    com o URI dinâmico do container (porta aleatória a cada execução)
 *
 * O container MongoDB é compartilhado entre todos os testes desta hierarquia
 * (static), o que melhora performance evitando subir/descer containers repetidamente.
 */
@Testcontainers
public abstract class BaseIntegrationTest {

    /**
     * Container MongoDB compartilhado entre todos os testes.
     * Usa a imagem oficial do MongoDB 6.
     */
    @Container
    static final MongoDBContainer mongoDBContainer = new MongoDBContainer("mongo:6.0")
            .withReuse(true);  // Reutiliza o container entre execuções para performance

    /**
     * Injeta dinamicamente o URI do MongoDB gerado pelo Testcontainers.
     * Sem isso, a aplicação tentaria conectar ao MongoDB em localhost:27017 (inexistente nos testes).
     */
    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
}