package com.likelion.tomorrowrich.expense.repository;

import com.likelion.tomorrowrich.expense.entity.Expense;
import com.likelion.tomorrowrich.user.entity.User;
import java.time.LocalDate;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExpenseRepository extends JpaRepository<Expense, Long> {

    List<Expense> findAllByUserAndExpenseDate(User user, LocalDate expenseDate);

    List<Expense> findAllByUserAndExpenseDateBetween(User user, LocalDate startDate, LocalDate endDate);
}
