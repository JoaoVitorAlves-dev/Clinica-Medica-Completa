package com.joao.vitor.alves.clinica.medica.clinica.dto.response;

import java.time.LocalDateTime;
import java.util.UUID;

public record PacienteResponse(
        UUID id,
        String usuarioNome,
        String convenioNome,
        LocalDateTime dataNascimento,
        String telefone
) {
}
