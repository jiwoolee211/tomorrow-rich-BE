package com.likelion.tomorrowrich.mission.dto;

public record MissionCompleteResponseDTO(
        Long missionId,
        String status,
        Integer rewardPoint,
        Integer currentPoint,
        Boolean pointReceived
) {
}
