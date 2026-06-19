package com.likelion.tomorrowrich.expense.dto;

public record ExpenseCreateRequestDTO(
        Integer amount,
        String categoryCode,
        String memo,
        String expenseDate
) {
}
