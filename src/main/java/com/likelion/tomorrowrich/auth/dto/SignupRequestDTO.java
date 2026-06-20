package com.likelion.tomorrowrich.auth.dto;

public record SignupRequestDTO(
        String loginId,
        String password,
        String passwordConfirm
) {
}