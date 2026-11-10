package com.joao.vitor.alves.clinica.medica.clinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MedicoRequest(
        @NotNull Long usuarioId,
        @NotBlank String crm,
        @NotNull Long especialidadeId
) {
}
