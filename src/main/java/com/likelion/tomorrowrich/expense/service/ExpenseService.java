package com.likelion.tomorrowrich.expense.service;

import com.likelion.tomorrowrich.category.entity.Category;
import com.likelion.tomorrowrich.category.repository.CategoryRepository;
import com.likelion.tomorrowrich.expense.dto.ExpenseCreateRequestDTO;
import com.likelion.tomorrowrich.expense.dto.ExpenseCreateResponseDTO;
import com.likelion.tomorrowrich.expense.dto.ExpenseUpdateRequestDTO;
import com.likelion.tomorrowrich.expense.dto.ExpenseUpdateResponseDTO;
import com.likelion.tomorrowrich.expense.entity.Expense;
import com.likelion.tomorrowrich.expense.repository.ExpenseRepository;
import com.likelion.tomorrowrich.global.exception.BusinessException;
import com.likelion.tomorrowrich.global.exception.ErrorCode;
import com.likelion.tomorrowrich.user.entity.User;
import com.likelion.tomorrowrich.user.repository.UserRepository;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeParseException;
import java.util.List;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class ExpenseService {

    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;

    public ExpenseService(
            ExpenseRepository expenseRepository,
            CategoryRepository categoryRepository,
            UserRepository userRepository
    ) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
    }

    public ExpenseCreateResponseDTO createExpense(Long userId, ExpenseCreateRequestDTO request) {
        validateAmount(request.amount());
        validateCategoryCode(request.categoryCode());

        LocalDate expenseDate = parseExpenseDate(request.expenseDate());
        validateNotFutureDate(expenseDate);

        User user = findUser(userId);
        Category category = findCategory(request.categoryCode());

        Expense expense = new Expense(
                user,
                category,
                request.amount(),
                request.memo(),
                expenseDate
        );

        Expense savedExpense = expenseRepository.save(expense);

        Integer monthlyExpenseAmount = calculateMonthlyExpenseAmount(user, expenseDate);
        Integer remainingBudget = calculateRemainingBudget(user, monthlyExpenseAmount);
        Integer todayAvailableAmount = calculateTodayAvailableAmount(expenseDate, remainingBudget);

        List<Long> updatedMissionIds = updateExpenseMissionProgress(user, savedExpense);

        return new ExpenseCreateResponseDTO(
                savedExpense.getId(),
                savedExpense.getAmount(),
                category.getCode(),
                category.getName(),
                savedExpense.getExpenseDate().toString(),
                monthlyExpenseAmount,
                remainingBudget,
                todayAvailableAmount,
                updatedMissionIds
        );
    }

    public ExpenseUpdateResponseDTO updateExpense(Long userId, Long expenseId, ExpenseUpdateRequestDTO request) {
        validateAmount(request.amount());
        validateCategoryCode(request.categoryCode());

        LocalDate expenseDate = parseExpenseDate(request.expenseDate());
        validateNotFutureDate(expenseDate);

        User user = findUser(userId);

        Expense expense = expenseRepository.findByIdAndUser(expenseId, user)
                .orElseThrow(() -> new BusinessException(ErrorCode.EXPENSE_NOT_FOUND));

        Category category = findCategory(request.categoryCode());

        expense.update(
                category,
                request.amount(),
                request.memo(),
                expenseDate
        );


        Integer monthlyExpenseAmount = calculateMonthlyExpenseAmount(user, expenseDate);
        calculateRemainingBudget(user, monthlyExpenseAmount);
        calculateTodayAvailableAmount(expenseDate, calculateRemainingBudget(user, monthlyExpenseAmount));

        return new ExpenseUpdateResponseDTO(true);
    }

    public String deleteExpense(Long userId, Long expenseId) {
        User user = findUser(userId);

        Expense expense = expenseRepository.findByIdAndUser(expenseId, user)
                .orElseThrow(() -> new BusinessException(ErrorCode.EXPENSE_NOT_FOUND));

        LocalDate expenseDate = expense.getExpenseDate();

        expenseRepository.delete(expense);


        Integer monthlyExpenseAmount = calculateMonthlyExpenseAmount(user, expenseDate);
        calculateRemainingBudget(user, monthlyExpenseAmount);
        calculateTodayAvailableAmount(expenseDate, calculateRemainingBudget(user, monthlyExpenseAmount));

        return "소비 기록 삭제 성공";
    }

    private void validateAmount(Integer amount) {
        if (amount == null || amount <= 0) {
            throw new BusinessException(ErrorCode.INVALID_EXPENSE_AMOUNT);
        }
    }

    private void validateCategoryCode(String categoryCode) {
        if (categoryCode == null || categoryCode.isBlank()) {
            throw new BusinessException(ErrorCode.CATEGORY_REQUIRED);
        }
    }

    private LocalDate parseExpenseDate(String expenseDate) {
        if (expenseDate == null || expenseDate.isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        try {
            return LocalDate.parse(expenseDate);
        } catch (DateTimeParseException exception) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }
    }

    private void validateNotFutureDate(LocalDate expenseDate) {
        if (expenseDate.isAfter(LocalDate.now())) {
            throw new BusinessException(ErrorCode.FUTURE_DATE_NOT_ALLOWED);
        }
    }

    private User findUser(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));
    }

    private Category findCategory(String categoryCode) {
        return categoryRepository.findByCode(categoryCode)
                .orElseThrow(() -> new BusinessException(ErrorCode.CATEGORY_REQUIRED));
    }

    private Integer calculateMonthlyExpenseAmount(User user, LocalDate expenseDate) {
        YearMonth yearMonth = YearMonth.from(expenseDate);
        LocalDate startDate = yearMonth.atDay(1);
        LocalDate endDate = yearMonth.atEndOfMonth();

        return expenseRepository.findAllByUserAndExpenseDateBetween(user, startDate, endDate)
                .stream()
                .mapToInt(Expense::getAmount)
                .sum();
    }

    private Integer calculateRemainingBudget(User user, Integer monthlyExpenseAmount) {
        Integer monthlyBudget = user.getMonthlyBudget();

        if (monthlyBudget == null) {
            monthlyBudget = 0;
        }

        return monthlyBudget - monthlyExpenseAmount;
    }

    private Integer calculateTodayAvailableAmount(LocalDate expenseDate, Integer remainingBudget) {
        LocalDate today = LocalDate.now();

        YearMonth currentMonth = YearMonth.from(today);
        YearMonth expenseMonth = YearMonth.from(expenseDate);

        if (!currentMonth.equals(expenseMonth)) {
            return remainingBudget;
        }

        int lastDayOfMonth = currentMonth.atEndOfMonth().getDayOfMonth();
        int todayDayOfMonth = today.getDayOfMonth();
        int remainingDays = lastDayOfMonth - todayDayOfMonth + 1;

        if (remainingDays <= 0) {
            return remainingBudget;
        }

        return remainingBudget / remainingDays;
    }

    private List<Long> updateExpenseMissionProgress(User user, Expense expense) {
        /*
         * 현재 미션 기능 연결 전이므로 빈 리스트를 반환함
         */
        return List.of();
    }
}