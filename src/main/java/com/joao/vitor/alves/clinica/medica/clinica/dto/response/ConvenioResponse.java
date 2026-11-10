package com.joao.vitor.alves.clinica.medica.clinica.dto.response;

public record ConvenioResponse(
        Long id,
        String nome,
        String cnpj,
        Boolean ativo
) {
}
