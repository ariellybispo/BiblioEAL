package com.library;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Ponto de entrada da aplicação Personal Library.
 *
 * @SpringBootApplication combina:
 * - @Configuration: classe de configuração Spring
 * - @EnableAutoConfiguration: configura automaticamente beans com base no classpath
 * - @ComponentScan: varre o pacote com.library e subpacotes por @Component, @Service, etc.
 */
@SpringBootApplication
public class PersonalLibraryApplication {

    public static void main(String[] args) {
        SpringApplication.run(PersonalLibraryApplication.class, args);
    }
}
