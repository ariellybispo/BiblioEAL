package com.library;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.zerodep.ZerodepDockerHttpClient;
import org.testcontainers.dockerclient.DockerClientProviderStrategy;
import org.testcontainers.dockerclient.InvalidConfigurationException;
import org.testcontainers.dockerclient.TransportConfig;

import java.net.URI;
import java.time.Duration;

/**
 * Estratégia customizada para Docker Desktop 29.x no Windows.
 *
 * Problema: docker-java usa API version 1.32 por padrão, mas Docker Desktop 29.x
 * exige mínimo 1.40 no pipe docker_engine.
 *
 * Solução: cria o DockerClient diretamente usando docker-java-core com
 * withApiVersion("1.41") explícito, sem depender de variáveis de ambiente.
 */
public class WindowsDockerStrategy extends DockerClientProviderStrategy {

    private static final URI  DOCKER_HOST = URI.create("npipe:////./pipe/docker_engine");
    private static final String API_VERSION = "1.41";

    @Override
    public String getDescription() {
        return "Windows Docker Desktop — docker_engine pipe, API " + API_VERSION;
    }

    @Override
    public TransportConfig getTransportConfig() throws InvalidConfigurationException {
        return TransportConfig.builder()
                .dockerHost(DOCKER_HOST)
                .build();
    }

    @Override
    protected int getPriority() {
        return Integer.MAX_VALUE;
    }

    @Override
    public DockerClient getDockerClient() {
        DefaultDockerClientConfig config = DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .withDockerHost(DOCKER_HOST.toString())
                .withApiVersion(API_VERSION)
                .build();

        ZerodepDockerHttpClient httpClient = new ZerodepDockerHttpClient.Builder()
                .dockerHost(DOCKER_HOST)
                .maxConnections(100)
                .connectionTimeout(Duration.ofSeconds(30))
                .responseTimeout(Duration.ofSeconds(45))
                .build();

        return DockerClientImpl.getInstance(config, httpClient);
    }
}
