package com.likelion.tomorrowrich.home.dto;

import java.util.List;

public record HomeResponseDTO(
        String nickname,
        String characterName,
        Integer monthlyBudget,
        Integer monthlySavingGoal,
        Integer monthlyExpenseAmount,
        Integer remainingBudget,
        Integer todayAvailableAmount,
        Integer point,
        String budgetStatus,
        SpeechBubble speechBubble,
        RoomInfo room,
        List<TodayMission> todayMissions
) {

    public record SpeechBubble(
            String type,
            String message,
            String level
    ) {
    }

    public record RoomInfo(
            String backgroundImageUrl,
            List<AppliedItem> appliedItems
    ) {
    }

    public record AppliedItem(
            Long itemId,
            String itemType,
            String name,
            String imageUrl
    ) {
    }

    public record TodayMission(
            Long missionId,
            String title,
            String description,
            Integer progress,
            Integer targetCount,
            Integer rewardPoint,
            String status
    ) {
    }
}