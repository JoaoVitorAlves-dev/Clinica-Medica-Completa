package com.joao.vitor.alves.clinica.medica.clinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.util.UUID;

public record PacienteRequest(
        @NotNull UUID usuarioId,
        @NotNull Long convenioId,
        @NotNull LocalDateTime dataNascimento,
        @NotBlank String telefone
) {
}
