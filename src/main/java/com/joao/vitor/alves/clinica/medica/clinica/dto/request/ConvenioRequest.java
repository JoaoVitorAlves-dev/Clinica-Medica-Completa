package com.joao.vitor.alves.clinica.medica.clinica.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ConvenioRequest(
        @NotBlank String nome,
        @NotBlank String cnpj
) {
}
