package com.joao.vitor.alves.clinica.medica.clinica.dto.request;

import com.joao.vitor.alves.clinica.medica.clinica.domain.roles.MetodoPagamento;
import com.joao.vitor.alves.clinica.medica.clinica.domain.roles.StatusPagamento;
import jakarta.validation.constraints.NotNull;

public record PagamentoRequest(
        @NotNull Long consultaId,
        @NotNull MetodoPagamento metodo
) {
}
