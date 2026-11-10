package com.joao.vitor.alves.clinica.medica.clinica.dto.response;

public record EnderecoResponse(
        Long id,
        String nomeUsuario,
        String rua,
        String cidade,
        String estado,
        String cep
) {
}
