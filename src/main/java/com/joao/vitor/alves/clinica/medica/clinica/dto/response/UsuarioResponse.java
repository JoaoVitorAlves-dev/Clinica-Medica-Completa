package com.joao.vitor.alves.clinica.medica.clinica.dto.response;

import java.util.UUID;

public record UsuarioResponse(
        UUID id,
        String nome,
        String cpf,
        String email
) {
}
