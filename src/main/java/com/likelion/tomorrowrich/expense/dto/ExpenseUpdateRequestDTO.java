package com.likelion.tomorrowrich.expense.dto;

public record ExpenseUpdateRequestDTO(
        Integer amount,
        String categoryCode,
        String memo,
        String expenseDate
) {
}