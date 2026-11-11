package com.joao.vitor.alves.clinica.medica.clinica.config;

import com.joao.vitor.alves.clinica.medica.clinica.service.TokenService;
import com.joao.vitor.alves.clinica.medica.clinica.service.UsuarioDetailsService;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class TokenFilter extends OncePerRequestFilter {

    private final UsuarioDetailsService usuarioDetailsService;
    private final TokenService tokenService;

    public TokenFilter(UsuarioDetailsService usuarioDetailsService, TokenService tokenService) {
        this.usuarioDetailsService = usuarioDetailsService;
        this.tokenService = tokenService;
    }

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {

        String token = extrairToken(request);

        try {
            if (token != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                String email = tokenService.validarToken(token);
                UserDetails usuario = usuarioDetailsService.loadUserByUsername(email);

                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(usuario, null, usuario.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (JwtException | IllegalArgumentException | UsernameNotFoundException e) {
            SecurityContextHolder.clearContext();
        }
        filterChain.doFilter(request, response);
    }

    private String extrairToken(HttpServletRequest req) {
        String authorization = req.getHeader("Authorization");

        if (authorization != null && authorization.startsWith("Bearer ")) {
            return authorization.substring(7);
        }
        return null;
    }

}
