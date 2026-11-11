package com.joao.vitor.alves.clinica.medica.clinica.dto.request;

import com.joao.vitor.alves.clinica.medica.clinica.domain.roles.StatusConsulta;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record ConsultaRequest(
        @NotNull UUID pacienteId,
        @NotNull Long medicoId,
        @NotNull LocalDateTime dataHora,
        @NotNull StatusConsulta status,
        @NotNull @PositiveOrZero BigDecimal valor
) {
}
