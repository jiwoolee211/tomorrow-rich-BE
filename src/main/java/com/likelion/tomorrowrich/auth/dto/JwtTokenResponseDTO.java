package com.likelion.tomorrowrich.auth.dto;

public record JwtTokenResponseDTO(
        String accessToken,
        Long userId,
        String nickname,
        Boolean onboardingCompleted
) {
}