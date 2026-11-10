package com.joao.vitor.alves.clinica.medica.clinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProntuarioRequest(
        @NotNull Long consultaId,
        @NotBlank String queixa,
        @NotBlank String diagnostico
) {
}
