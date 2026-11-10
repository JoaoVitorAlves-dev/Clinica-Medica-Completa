package com.joao.vitor.alves.clinica.medica.clinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ExameRequest(
        @NotNull Long prontuarioId,
        @NotBlank String tipo,
        @NotBlank String resultado
) {
}
