package com.joao.vitor.alves.clinica.medica.clinica.repository;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Paciente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface PacienteRepository extends JpaRepository<Paciente, UUID> {
}
