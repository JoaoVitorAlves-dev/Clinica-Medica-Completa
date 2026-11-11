package com.joao.vitor.alves.clinica.medica.clinica.config;

import org.hibernate.boot.jaxb.mapping.internal.GenerationTimingMarshalling;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final TokenFilter tokenFilter;

    public SecurityConfig(TokenFilter tokenFilter) {
        this.tokenFilter = tokenFilter;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) {
        http.csrf(csrf -> csrf.disable()
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth

                        .requestMatchers(HttpMethod.POST,"/usuarios/refresh", "/usuarios/registrar", "/usuarios/logar").permitAll()
                        .requestMatchers(HttpMethod.DELETE, "/usuarios/deletar/{id}").authenticated()
                        .requestMatchers(HttpMethod.GET, "/usuarios/listar").authenticated()

                        .anyRequest().authenticated())
                .addFilterBefore(tokenFilter, UsernamePasswordAuthenticationFilter.class)
        );
        return http.build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) {
        return configuration.getAuthenticationManager();
    }

}
