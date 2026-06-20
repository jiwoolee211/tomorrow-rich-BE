package com.likelion.tomorrowrich.onboarding.controller;

import com.likelion.tomorrowrich.global.security.SecurityUtil;
import com.likelion.tomorrowrich.onboarding.dto.OnboardingRequestDTO;
import com.likelion.tomorrowrich.onboarding.dto.OnboardingResponseDTO;
import com.likelion.tomorrowrich.onboarding.service.OnboardingService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/onboarding")
public class OnboardingController {

    private final OnboardingService onboardingService;
    private final SecurityUtil securityUtil;

    public OnboardingController(
            OnboardingService onboardingService,
            SecurityUtil securityUtil
    ) {
        this.onboardingService = onboardingService;
        this.securityUtil = securityUtil;
    }

    @PostMapping
    public ResponseEntity<OnboardingResponseDTO> saveOnboarding(
            @RequestBody OnboardingRequestDTO request
    ) {
        Long userId = securityUtil.getCurrentUserId();

        OnboardingResponseDTO response = onboardingService.saveOnboarding(userId, request);

        return ResponseEntity.ok(response);
    }
}