package com.joao.vitor.alves.clinica.medica.clinica.dto.response;

public record ProntuarioResponse(
        Long id,
        Long consultaId,
        String queixa,
        String diagnostico
) {
}
