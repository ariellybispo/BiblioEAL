package com.library.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.library.dto.ExternalBookInfoDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;

/**
 * Serviço de integração com a API externa Open Library (openlibrary.org).
 *
 * Responsabilidade: buscar metadados de um livro pelo ISBN via HTTP.
 *
 * Nos testes, o baseUrl é sobrescrito pela propriedade de teste para apontar
 * ao WireMock, que reproduz cassettes (respostas pré-gravadas) — padrão VCR.
 */
@Slf4j
@Service
public class ExternalBookService {

    private final RestTemplate restTemplate;
    private final ObjectMapper objectMapper;
    private final String baseUrl;

    public ExternalBookService(
            RestTemplate restTemplate,
            ObjectMapper objectMapper,
            @Value("${openlibrary.api.base-url:https://openlibrary.org}") String baseUrl) {
        this.restTemplate = restTemplate;
        this.objectMapper = objectMapper;
        this.baseUrl = baseUrl;
    }

    /**
     * Busca informações de um livro pelo ISBN na API Open Library.
     *
     * Endpoint: GET /api/books?bibkeys=ISBN:{isbn}&format=json&jscmd=data
     *
     * @param isbn ISBN do livro (ex: 9780132350884)
     * @return Optional com os dados do livro, ou vazio se não encontrado / erro
     */
    public Optional<ExternalBookInfoDTO> fetchBookInfoByIsbn(String isbn) {
        try {
            String url = baseUrl + "/api/books?bibkeys=ISBN:" + isbn + "&format=json&jscmd=data";
            String responseJson = restTemplate.getForObject(url, String.class);

            if (responseJson == null || responseJson.isBlank() || "{}".equals(responseJson.trim())) {
                log.debug("Open Library: nenhum resultado para ISBN {}", isbn);
                return Optional.empty();
            }

            JsonNode root = objectMapper.readTree(responseJson);
            JsonNode bookNode = root.get("ISBN:" + isbn);

            if (bookNode == null || bookNode.isNull() || bookNode.isEmpty()) {
                return Optional.empty();
            }

            String title = bookNode.path("title").asText(null);

            String author = null;
            JsonNode authors = bookNode.path("authors");
            if (authors.isArray() && !authors.isEmpty()) {
                author = authors.get(0).path("name").asText(null);
            }

            String publishDate = bookNode.path("publish_date").asText(null);

            return Optional.of(ExternalBookInfoDTO.builder()
                    .title(title)
                    .author(author)
                    .publishDate(publishDate)
                    .build());

        } catch (Exception e) {
            log.warn("Erro ao consultar Open Library para ISBN {}: {}", isbn, e.getMessage());
            return Optional.empty();
        }
    }
}
