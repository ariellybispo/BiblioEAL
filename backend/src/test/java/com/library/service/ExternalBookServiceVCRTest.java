package com.library.service;

import com.github.tomakehurst.wiremock.junit5.WireMockTest;
import com.library.BaseIntegrationTest;
import com.library.dto.ExternalBookInfoDTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;

import java.util.Optional;

import static com.github.tomakehurst.wiremock.client.WireMock.*;
import static org.assertj.core.api.Assertions.*;

/**
 * Testes VCR (Video Cassette Recorder) para ExternalBookService.
 *
 * ========================
 * PADRÃO VCR COM WIREMOCK
 * ========================
 * VCR = gravar respostas HTTP reais e reproduzi-las nos testes.
 *
 * Analogia:
 *   - Cassette = arquivo JSON com a resposta HTTP pré-gravada
 *   - Play     = WireMock reproduzindo o cassette quando requisitado
 *
 * Fluxo:
 *   1. O serviço faz HTTP GET para http://localhost:8090/api/books?...
 *   2. WireMock intercepta e retorna o cassette (JSON pré-gravado)
 *   3. O serviço processa a resposta como se fosse da API real
 *
 * Por que VCR em vez de chamada real?
 *   - Testes determinísticos (sem dependência de rede)
 *   - Sem consumo de API (rate limits, latência)
 *   - Reproduzível em CI/CD sem acesso à internet
 *
 * Os cassettes (arquivos JSON) estão em: src/test/resources/__files/
 *
 * ========================
 * TIPO: Caixa Preta + VCR
 * ========================
 * - Caixa Preta: testa entradas/saídas sem conhecer internals
 * - VCR: respostas HTTP simuladas via cassettes WireMock
 */
@SpringBootTest
@ActiveProfiles("test")
@WireMockTest(httpPort = 8090)
@TestPropertySource(properties = {
    "openlibrary.api.base-url=http://localhost:8090"
})
@DisplayName("ExternalBookService — Testes VCR (WireMock Cassette)")
class ExternalBookServiceVCRTest extends BaseIntegrationTest {

    @Autowired
    private ExternalBookService externalBookService;

    // ========================
    // SUCESSO — Cassette com dados reais
    // ========================

    @Test
    @DisplayName("Deve retornar dados do livro ao reproduzir cassette do Open Library")
    void shouldFetchBookInfoFromCassette() {
        // ARRANGE — configura o WireMock para reproduzir o cassette pré-gravado
        stubFor(get(urlPathEqualTo("/api/books"))
                .withQueryParam("bibkeys", equalTo("ISBN:9780132350884"))
                .withQueryParam("format", equalTo("json"))
                .withQueryParam("jscmd", equalTo("data"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("open-library-isbn-9780132350884.json")));

        // ACT
        Optional<ExternalBookInfoDTO> result = externalBookService.fetchBookInfoByIsbn("9780132350884");

        // ASSERT — verifica que o serviço parseou corretamente o cassette
        assertThat(result).isPresent();
        assertThat(result.get().getTitle()).isEqualTo("Clean Code");
        assertThat(result.get().getAuthor()).isEqualTo("Robert C. Martin");
        assertThat(result.get().getPublishDate()).isEqualTo("2008");
    }

    // ========================
    // ISBN NÃO ENCONTRADO — Cassette vazio
    // ========================

    @Test
    @DisplayName("Deve retornar Optional.empty() quando cassette retorna body vazio (ISBN desconhecido)")
    void shouldReturnEmptyWhenIsbnNotFound() {
        // Cassette simula resposta da API para ISBN inexistente: corpo "{}"
        stubFor(get(urlPathEqualTo("/api/books"))
                .withQueryParam("bibkeys", containing("ISBN:isbn-inexistente"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBodyFile("open-library-isbn-not-found.json")));

        Optional<ExternalBookInfoDTO> result = externalBookService.fetchBookInfoByIsbn("isbn-inexistente");

        assertThat(result).isEmpty();
    }

    // ========================
    // ERRO NA API — Cassette de falha
    // ========================

    @Test
    @DisplayName("Deve retornar Optional.empty() quando API retorna HTTP 500 (resiliência)")
    void shouldReturnEmptyWhenApiReturnsServerError() {
        stubFor(get(urlPathEqualTo("/api/books"))
                .willReturn(aResponse()
                        .withStatus(500)
                        .withBody("Internal Server Error")));

        Optional<ExternalBookInfoDTO> result = externalBookService.fetchBookInfoByIsbn("qualquer-isbn");

        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("Deve retornar Optional.empty() quando API retorna HTTP 404")
    void shouldReturnEmptyWhenApiReturnsNotFound() {
        stubFor(get(urlPathEqualTo("/api/books"))
                .willReturn(aResponse()
                        .withStatus(404)));

        Optional<ExternalBookInfoDTO> result = externalBookService.fetchBookInfoByIsbn("isbn-404");

        assertThat(result).isEmpty();
    }

    // ========================
    // TESTES PARAMETRIZADOS — Múltiplos ISBNs inválidos
    // ========================

    @ParameterizedTest(name = "ISBN inválido [{0}] deve retornar Optional.empty()")
    @ValueSource(strings = {"", "0000000000000", "isbn-invalido", "????"})
    @DisplayName("Deve retornar vazio para ISBNs sem resultado na API (parametrizado)")
    void shouldReturnEmptyForIsbnsThatYieldNoResults(String isbn) {
        stubFor(get(urlPathEqualTo("/api/books"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withHeader("Content-Type", "application/json")
                        .withBody("{}")));

        Optional<ExternalBookInfoDTO> result = externalBookService.fetchBookInfoByIsbn(isbn);

        assertThat(result).isEmpty();
    }

    // ========================
    // VERIFICAÇÃO — WireMock confirma que a chamada foi feita
    // ========================

    @Test
    @DisplayName("Deve confirmar que o serviço realizou a chamada HTTP ao endpoint correto")
    void shouldVerifyHttpCallWasMade() {
        stubFor(get(urlPathEqualTo("/api/books"))
                .willReturn(aResponse()
                        .withStatus(200)
                        .withBody("{}")));

        externalBookService.fetchBookInfoByIsbn("9780132350884");

        // Verifica que WireMock recebeu exatamente 1 requisição HTTP
        verify(1, getRequestedFor(urlPathEqualTo("/api/books"))
                .withQueryParam("bibkeys", equalTo("ISBN:9780132350884"))
                .withQueryParam("format", equalTo("json"))
                .withQueryParam("jscmd", equalTo("data")));
    }
}
