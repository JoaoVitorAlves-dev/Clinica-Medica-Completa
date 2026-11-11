package com.joao.vitor.alves.clinica.medica.clinica.dto.response;

public record RefreshTokenResponse(
        String accessToken,
        String refreshToken
) {
}
