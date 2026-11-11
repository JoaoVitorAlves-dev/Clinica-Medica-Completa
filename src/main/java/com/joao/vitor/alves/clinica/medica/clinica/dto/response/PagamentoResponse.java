package com.joao.vitor.alves.clinica.medica.clinica.dto.response;

import com.joao.vitor.alves.clinica.medica.clinica.domain.roles.MetodoPagamento;
import com.joao.vitor.alves.clinica.medica.clinica.domain.roles.StatusPagamento;

import java.util.UUID;

public record PagamentoResponse(
        UUID id,
        Long consultaId,
        MetodoPagamento metodo,
        StatusPagamento status
) {
}
