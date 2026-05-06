package com.library.service;

import com.library.dto.UserRegistrationDTO;
import com.library.model.User;
import com.library.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

/**
 * Serviço de Usuários.
 *
 * Implementa UserDetailsService para integração com Spring Security.
 * O Spring Security chama loadUserByUsername() automaticamente durante o login.
 */
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    /**
     * Método exigido pelo Spring Security para autenticação.
     * Busca o usuário pelo username e retorna um objeto UserDetails
     * que o Spring usa para verificar a senha e os papéis.
     *
     * @throws UsernameNotFoundException se o usuário não existir
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(
                        "Usuário não encontrado: " + username));

        // Converte roles do MongoDB para authorities do Spring Security
        List<SimpleGrantedAuthority> authorities = user.getRoles().stream()
                .map(SimpleGrantedAuthority::new)
                .toList();

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                authorities
        );
    }

    /**
     * Registra um novo usuário no sistema.
     *
     * Fluxo:
     * 1. Valida se as senhas conferem
     * 2. Verifica se username/email já estão em uso
     * 3. Criptografa a senha com BCrypt
     * 4. Persiste o usuário com role padrão ROLE_USER
     *
     * @throws IllegalArgumentException se houver violação de regras de negócio
     */
    public User register(UserRegistrationDTO dto) {
        // Regra 1: senhas devem ser iguais
        if (!dto.passwordsMatch()) {
            throw new IllegalArgumentException("As senhas não conferem");
        }

        // Regra 2: username único
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException("Username já está em uso");
        }

        // Regra 3: email único
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException("Email já está cadastrado");
        }

        User user = User.builder()
                .name(dto.getName())
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))  // NUNCA salva senha pura!
                .roles(List.of("ROLE_USER"))
                .createdAt(LocalDateTime.now())
                .build();

        return userRepository.save(user);
    }

    /**
     * Busca usuário pelo username (usado internamente para obter o ID do usuário autenticado).
     */
    public User findByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado: " + username));
    }
}