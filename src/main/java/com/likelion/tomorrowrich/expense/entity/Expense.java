package com.likelion.tomorrowrich.expense.entity;

import com.likelion.tomorrowrich.category.entity.Category;
import com.likelion.tomorrowrich.user.entity.User;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "expenses")
public class Expense {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private Integer amount;

    private String memo;

    @Column(nullable = false)
    private LocalDate expenseDate;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected Expense() {
    }

    public Expense(User user, Category category, Integer amount, String memo, LocalDate expenseDate) {
        this.user = user;
        this.category = category;
        this.amount = amount;
        this.memo = memo;
        this.expenseDate = expenseDate;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public Category getCategory() {
        return category;
    }

    public Integer getAmount() {
        return amount;
    }

    public String getMemo() {
        return memo;
    }

    public LocalDate getExpenseDate() {
        return expenseDate;
    }

    public void update(Category category, Integer amount, String memo, LocalDate expenseDate) {
        this.category = category;
        this.amount = amount;
        this.memo = memo;
        this.expenseDate = expenseDate;
    }
}
