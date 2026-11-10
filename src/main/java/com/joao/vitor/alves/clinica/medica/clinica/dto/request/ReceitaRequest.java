package com.joao.vitor.alves.clinica.medica.clinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ReceitaRequest(
        @NotNull Long prontuarioId,
        @NotBlank String medicamento,
        @NotBlank String posologia
) {
}
