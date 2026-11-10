package com.joao.vitor.alves.clinica.medica.clinica.repository;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Prontuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProntuarioRepository extends JpaRepository<Prontuario, Long> {
}
