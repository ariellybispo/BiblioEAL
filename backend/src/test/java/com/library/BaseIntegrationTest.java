package com.library;

import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.containers.MongoDBContainer;
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
 * 2. Container é iniciado ESTATICAMENTE (uma vez para toda a JVM)
 * 3. @DynamicPropertySource: sobrescreve as propriedades do Spring
 *    com o URI dinâmico do container (porta aleatória a cada execução)
 *
 * O container MongoDB vive durante toda a suite de testes (via static block +
 * shutdown hook), evitando que o Spring context tente reconectar a uma porta
 * que já fechou quando as classes de teste rodam sequencialmente.
 */
@Testcontainers
public abstract class BaseIntegrationTest {

    /**
     * Container MongoDB singleton compartilhado por TODA a suite de testes.
     *
     * Iniciado no static block (uma vez por JVM) e parado via shutdown hook.
     * Usar @Container aqui causaria reinício entre classes de teste, fazendo
     * o Spring context perder a conexão com a porta antiga.
     */
    static final MongoDBContainer mongoDBContainer;

    static {
        mongoDBContainer = new MongoDBContainer("mongo:6.0");
        mongoDBContainer.start();
        Runtime.getRuntime().addShutdownHook(new Thread(mongoDBContainer::stop));
    }

    @DynamicPropertySource
    static void setProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.data.mongodb.uri", mongoDBContainer::getReplicaSetUrl);
    }
}
