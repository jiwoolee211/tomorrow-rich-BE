package com.likelion.tomorrowrich.expense.dto;

import java.util.List;

public record ExpenseCreateResponseDTO(
        Long expenseId,
        Integer amount,
        String categoryCode,
        String categoryName,
        String expenseDate,
        Integer monthlyExpenseAmount,
        Integer remainingBudget,
        Integer todayAvailableAmount,
        /*
         * TODO:
         MissionService 구현 후 소비 등록으로 진행률이 변경된 미션 ID 목록을 반환하도록 수정
         */
        List<Long> updatedMissionIds
) {
}