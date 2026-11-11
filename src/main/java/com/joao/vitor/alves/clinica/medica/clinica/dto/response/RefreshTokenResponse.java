package com.joao.vitor.alves.clinica.medica.clinica.dto.response;

import jakarta.validation.constraints.NotBlank;

public record RefreshTokenResponse(
        @NotBlank String accessToken,
        @NotBlank String refreshToken
) {
}
