package com.likelion.tomorrowrich.user.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import java.time.LocalDateTime;

@Entity
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String loginId;

    @Column(nullable = false)
    private String password;

    @Column(length = 30)
    private String nickname;

    @Column(length = 30)
    private String characterName;

    private Integer monthlyBudget;

    private Integer monthlySavingGoal;

    private Integer fixedExpense;

    @Column(nullable = false)
    private Integer currentPoint = 0;

    @Column(nullable = false)
    private Boolean onboardingCompleted = false;

    @Column(nullable = false)
    private Boolean missionNotification = true;

    @Column(nullable = false)
    private Boolean expenseRecordNotification = true;

    @Column(nullable = false, updatable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    protected User() {
    }

    public User(String loginId, String password) {
        this.loginId = loginId;
        this.password = password;
    }

    public Long getId() {
        return id;
    }

    public String getLoginId() {
        return loginId;
    }

    public String getPassword() {
        return password;
    }

    public String getNickname() {
        return nickname;
    }

    public String getCharacterName() {
        return characterName;
    }

    public Integer getMonthlyBudget() {
        return monthlyBudget;
    }

    public Integer getMonthlySavingGoal() {
        return monthlySavingGoal;
    }

    public Integer getFixedExpense() {
        return fixedExpense;
    }

    public Integer getCurrentPoint() {
        return currentPoint;
    }

    public Boolean getOnboardingCompleted() {
        return onboardingCompleted;
    }

    public Boolean getMissionNotification() {
        return missionNotification;
    }

    public Boolean getExpenseRecordNotification() {
        return expenseRecordNotification;
    }

    public void completeOnboarding(
            String nickname,
            String characterName,
            Integer monthlyBudget,
            Integer monthlySavingGoal,
            Integer fixedExpense
    ) {
        this.nickname = nickname;
        this.characterName = characterName;
        this.monthlyBudget = monthlyBudget;
        this.monthlySavingGoal = monthlySavingGoal;
        this.fixedExpense = fixedExpense;
        this.onboardingCompleted = true;
    }

    public void updateBudget(Integer monthlyBudget, Integer monthlySavingGoal, Integer fixedExpense) {
        this.monthlyBudget = monthlyBudget;
        this.monthlySavingGoal = monthlySavingGoal;
        this.fixedExpense = fixedExpense;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateNotification(Boolean missionNotification, Boolean expenseRecordNotification) {
        this.missionNotification = missionNotification;
        this.expenseRecordNotification = expenseRecordNotification;
    }

    public void addPoint(Integer amount) {
        this.currentPoint += amount;
    }

    public void spendPoint(Integer amount) {
        this.currentPoint -= amount;
    }
}
