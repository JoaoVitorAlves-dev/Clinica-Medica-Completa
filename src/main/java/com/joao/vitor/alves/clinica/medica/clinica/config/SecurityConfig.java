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
                        .requestMatchers(HttpMethod.GET, "/usuarios/listar", "/enderecos/listar").authenticated()

                        .requestMatchers(HttpMethod.POST, "/enderecos/criar").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/enderecos/atualizar/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/enderecos/deletar/{id}").authenticated()

                        .requestMatchers(HttpMethod.GET, "/medicos/listar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/medicos/criar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/medicos/atualizar/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/medicos/deletar/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/especialidades/listar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/especialidades/criar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/especialidades/atualizar/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/especialidades/desativar/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/especialidades/listar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/especialidades/criar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/especialidades/atualizar/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/especialidades/desativar/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/convenios/listar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/convenios/criar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/convenios/atualizar/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/convenios/desativar/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/pacientes/listar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/pacientes/cadastrar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/pacientes/atualizar/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/pacientes/deletar/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/pacientes/visualizar").authenticated()
                        .requestMatchers(HttpMethod.POST, "/pacientes/cadastro").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/pacientes/atualizarcadastro/{id}").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/pacientes/deletarcadastro/{id}").authenticated()

                        .requestMatchers(HttpMethod.GET, "/consulta/listar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.POST, "/consulta/adicionar").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.PATCH, "/consulta/atualizar/{id}").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.DELETE, "/consulta/deletar/{id}").hasRole("ADMIN")

                        .requestMatchers(HttpMethod.GET, "/pagamentos/listar").authenticated()
                        .requestMatchers(HttpMethod.POST, "/pagamentos/adicionar").authenticated()
                        .requestMatchers(HttpMethod.PATCH, "/pagamentos/atualizar/{id}").authenticated()
                        .requestMatchers(HttpMethod.DELETE, "/pagamentos/deletar/{id}").authenticated()

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
