package com.joao.vitor.alves.clinica.medica.clinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record EnderecoRequest(
        UUID usuarioId,
        @NotBlank String rua,
        @NotBlank String cidade,
        @NotBlank String estado,
        @NotBlank String cep
) {
}
