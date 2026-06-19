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
        List<Long> updatedMissionIds
) {
}