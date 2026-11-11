package com.joao.vitor.alves.clinica.medica.clinica.service;

import com.joao.vitor.alves.clinica.medica.clinica.domain.RefreshToken;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Usuario;
import com.joao.vitor.alves.clinica.medica.clinica.repository.RefreshTokenRepository;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.UUID;

@Service
public class RefreshTokenService {

    @Value("${api.token.expiration-in-days}")
    private long expiration;

    private final RefreshTokenRepository refreshTokenRepository;

    public RefreshTokenService(RefreshTokenRepository refreshTokenRepository) {
        this.refreshTokenRepository = refreshTokenRepository;
    }

    @Transactional
    public RefreshToken criar(Usuario usuario) {
        refreshTokenRepository.deleteByUsuario(usuario);

        RefreshToken rt = new RefreshToken();
        rt.setUsuario(usuario);
        rt.setToken(UUID.randomUUID().toString());
        rt.setExpiraEm(Instant.now().plus(expiration, ChronoUnit.DAYS));
        return refreshTokenRepository.save(rt);
    }

    @Transactional
    public RefreshToken validarERotacionar(String token) {
        RefreshToken refreshToken = refreshTokenRepository.findByToken(token)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

        if (refreshToken.getExpiraEm().isBefore(Instant.now())) {
            refreshTokenRepository.delete(refreshToken);
            throw new ResponseStatusException(HttpStatus.FORBIDDEN);
        }

        return criar(refreshToken.getUsuario());
    }

    @Transactional
    public void apagarPorToken(String token) {
        refreshTokenRepository.findByToken(token).ifPresent(refreshTokenRepository::delete);
    }

}
