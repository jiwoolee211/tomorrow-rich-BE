package com.likelion.tomorrowrich.mission.dto;

import java.util.List;

public record MissionListResponseDTO(
        String date,
        Boolean isFuture,
        List<MissionDTO> todayMissions,
        List<MissionDTO> dailyMissions
) {
    public record MissionDTO(
            Long missionId,
            String title,
            String description,
            String missionType,
            Integer progress,
            Integer targetCount,
            Integer rewardPoint,
            String status,
            Boolean pointReceived
    ) {
    }
}