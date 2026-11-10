package com.joao.vitor.alves.clinica.medica.clinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record MedicoRequest(
        @NotNull UUID usuarioId,
        @NotBlank String crm,
        @NotNull Long especialidadeId
) {
}
