package com.likelion.tomorrowrich.expense.controller;

import com.likelion.tomorrowrich.expense.dto.ExpenseCreateRequestDTO;
import com.likelion.tomorrowrich.expense.dto.ExpenseCreateResponseDTO;
import com.likelion.tomorrowrich.expense.dto.ExpenseUpdateRequestDTO;
import com.likelion.tomorrowrich.expense.dto.ExpenseUpdateResponseDTO;
import com.likelion.tomorrowrich.expense.service.ExpenseService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/expenses")
public class ExpenseController {

    private final ExpenseService expenseService;

    public ExpenseController(ExpenseService expenseService) {
        this.expenseService = expenseService;
    }

    @PostMapping
    public ResponseEntity<ExpenseCreateResponseDTO> createExpense(
            @RequestBody ExpenseCreateRequestDTO request
    ) {
        Long userId = getLoginUserId();

        ExpenseCreateResponseDTO response = expenseService.createExpense(userId, request);

        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{expenseId}")
    public ResponseEntity<ExpenseUpdateResponseDTO> updateExpense(
            @PathVariable Long expenseId,
            @RequestBody ExpenseUpdateRequestDTO request
    ) {
        Long userId = getLoginUserId();

        ExpenseUpdateResponseDTO response = expenseService.updateExpense(userId, expenseId, request);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{expenseId}")
    public ResponseEntity<String> deleteExpense(
            @PathVariable Long expenseId
    ) {
        Long userId = getLoginUserId();

        String response = expenseService.deleteExpense(userId, expenseId);

        return ResponseEntity.ok(response);
    }

    private Long getLoginUserId() {
        /*
         * TODO:
         * 현재는 임시 사용자 id임
         */
        return 1L;
    }
}