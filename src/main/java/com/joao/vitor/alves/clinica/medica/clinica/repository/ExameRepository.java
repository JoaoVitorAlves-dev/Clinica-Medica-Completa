package com.joao.vitor.alves.clinica.medica.clinica.repository;

import com.joao.vitor.alves.clinica.medica.clinica.domain.Exame;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExameRepository extends JpaRepository<Exame, Long> {
}
