package com.likelion.tomorrowrich.onboarding.dto;

public record OnboardingRequestDTO(
        String nickname,
        String characterName,
        Integer monthlyBudget,
        Integer monthlySavingGoal,
        Integer fixedExpense
) {
}