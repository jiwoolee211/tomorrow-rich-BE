package com.likelion.tomorrowrich.onboarding.service;

import com.likelion.tomorrowrich.global.exception.BusinessException;
import com.likelion.tomorrowrich.global.exception.ErrorCode;
import com.likelion.tomorrowrich.onboarding.dto.OnboardingRequestDTO;
import com.likelion.tomorrowrich.onboarding.dto.OnboardingResponseDTO;
import com.likelion.tomorrowrich.user.entity.User;
import com.likelion.tomorrowrich.user.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class OnboardingService {

    private final UserRepository userRepository;

    public OnboardingService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public OnboardingResponseDTO saveOnboarding(
            Long userId,
            OnboardingRequestDTO request
    ) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        validateOnboardingRequest(request);

        if (Boolean.TRUE.equals(user.getOnboardingCompleted())) {
            throw new BusinessException(ErrorCode.ALREADY_ONBOARDED);
        }

        user.completeOnboarding(
                request.nickname(),
                request.characterName(),
                request.monthlyBudget(),
                request.monthlySavingGoal(),
                request.fixedExpense()
        );

        return new OnboardingResponseDTO(user.getOnboardingCompleted());
    }

    private void validateOnboardingRequest(OnboardingRequestDTO request) {
        if (request.nickname() == null || request.nickname().isBlank()) {
            throw new BusinessException(ErrorCode.EMPTY_NICKNAME);
        }

        if (request.characterName() == null || request.characterName().isBlank()) {
            throw new BusinessException(ErrorCode.EMPTY_CHARACTER_NAME);
        }

        if (request.monthlyBudget() == null || request.monthlyBudget() < 0) {
            throw new BusinessException(ErrorCode.INVALID_BUDGET);
        }

        if (request.monthlySavingGoal() == null || request.monthlySavingGoal() < 0) {
            throw new BusinessException(ErrorCode.INVALID_BUDGET);
        }

        if (request.fixedExpense() != null && request.fixedExpense() < 0) {
            throw new BusinessException(ErrorCode.INVALID_BUDGET);
        }
    }
}