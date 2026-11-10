package com.joao.vitor.alves.clinica.medica.clinica.dto.response;

import com.joao.vitor.alves.clinica.medica.clinica.domain.roles.StatusConsulta;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record ConsultaResponse(
        Long id,
        String pacienteNome,
        String medicoNome,
        LocalDateTime dataHora,
        StatusConsulta status,
        BigDecimal valor
) {
}
