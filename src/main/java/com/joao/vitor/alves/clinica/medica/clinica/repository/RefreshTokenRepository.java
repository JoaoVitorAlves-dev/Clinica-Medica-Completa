package com.joao.vitor.alves.clinica.medica.clinica.repository;

import com.joao.vitor.alves.clinica.medica.clinica.domain.RefreshToken;
import com.joao.vitor.alves.clinica.medica.clinica.domain.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface RefreshTokenRepository extends JpaRepository<RefreshToken, UUID> {

    void deleteByUsuario(Usuario usuario);

    Optional<RefreshToken> findByToken(String token);

}
