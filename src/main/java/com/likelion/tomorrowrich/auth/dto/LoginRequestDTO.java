package com.likelion.tomorrowrich.auth.dto;

public record LoginRequestDTO(
        String loginId,
        String password
) {
}