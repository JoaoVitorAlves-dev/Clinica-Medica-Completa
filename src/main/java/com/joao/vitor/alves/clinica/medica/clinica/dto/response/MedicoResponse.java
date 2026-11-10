package com.joao.vitor.alves.clinica.medica.clinica.dto.response;

public record MedicoResponse(
        Long id,
        String usuarioNome,
        String crm,
        String EspecialidadeNome
) {
}
