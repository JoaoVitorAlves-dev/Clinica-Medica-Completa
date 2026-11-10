package com.joao.vitor.alves.clinica.medica.clinica.dto.response;

public record ExameResponse(
        Long id,
        Long prontuarioId,
        String tipo,
        String resultado
) {
}
