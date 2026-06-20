package com.likelion.tomorrowrich.auth.dto;

public record SignupResponseDTO(
        Long userId,
        String loginId,
        Boolean onboardingCompleted
) {
}