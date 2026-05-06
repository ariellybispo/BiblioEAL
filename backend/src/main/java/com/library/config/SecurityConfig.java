package com.library.config;

import com.library.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configuração de Segurança da Aplicação.
 *
 * Define:
 * - Quais rotas são públicas vs. protegidas
 * - Como o login e logout funcionam
 * - Qual PasswordEncoder usar (BCrypt)
 * - Como o Spring Security autentica usuários (via UserDetailsService)
 *
 * @EnableWebSecurity: habilita a segurança web do Spring Security
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final UserService userService;

    /**
     * Bean do PasswordEncoder.
     * BCrypt é o padrão ouro para hash de senhas:
     * - Inclui salt automático (proteção contra rainbow tables)
     * - Custo configurável (por padrão: 10 rounds = 2^10 iterações)
     *
     * IMPORTANTE: Este bean é separado do SecurityConfig para evitar
     * dependência circular (UserService → SecurityConfig → UserService).
     */
    @Bean
    public static PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura as regras de acesso HTTP e o comportamento de login/logout.
     *
     * Regras (ordem importa — a primeira que bate é aplicada):
     * 1. /css/**, /js/** → público (arquivos estáticos)
     * 2. /login, /register → público (páginas de autenticação)
     * 3. Qualquer outra rota → requer autenticação
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests(auth -> auth
                // Recursos estáticos são públicos
                .requestMatchers("/css/**", "/js/**", "/images/**").permitAll()
                // Páginas de autenticação são públicas
                .requestMatchers("/login", "/register").permitAll()
                // Tudo o mais requer autenticação
                .anyRequest().authenticated()
            )
            .formLogin(form -> form
                .loginPage("/login")                    // Nossa página de login customizada
                .defaultSuccessUrl("/books", true)      // Redireciona para /books após login
                .failureUrl("/login?error=true")        // Redireciona com erro se falhar
                .permitAll()
            )
            .logout(logout -> logout
                .logoutUrl("/logout")
                .logoutSuccessUrl("/login?logout=true") // Mensagem de logout bem-sucedido
                .invalidateHttpSession(true)            // Invalida a sessão HTTP
                .deleteCookies("JSESSIONID")            // Remove o cookie de sessão
                .permitAll()
            )
            // Gerenciamento de sessão: máximo 1 sessão por usuário
            .sessionManagement(session -> session
                .maximumSessions(1)
            );

        return http.build();
    }

    /**
     * Provedor de autenticação que usa o UserDetailsService (UserService)
     * para carregar os dados do usuário e o PasswordEncoder para verificar a senha.
     */
    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    /**
     * AuthenticationManager exposto como Bean para uso em testes e outros contextos.
     */
    @Bean
    public AuthenticationManager authenticationManager(
            AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }
}
