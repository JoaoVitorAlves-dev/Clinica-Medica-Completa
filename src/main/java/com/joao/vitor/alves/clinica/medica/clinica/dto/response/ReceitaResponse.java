package com.joao.vitor.alves.clinica.medica.clinica.dto.response;

public record ReceitaResponse(
        Long id,
        Long prontuarioId,
        String medicamento,
        String posologia
) {
}
