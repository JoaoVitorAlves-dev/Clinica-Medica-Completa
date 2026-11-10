package com.joao.vitor.alves.clinica.medica.clinica.dto.response;

import java.time.LocalDateTime;

public record PacienteResponse(
        Long id,
        String usuarioNome,
        String convenioNome,
        LocalDateTime dataNascimento,
        String telefone
) {
}
