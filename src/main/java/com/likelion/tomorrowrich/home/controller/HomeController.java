package com.likelion.tomorrowrich.home.controller;

import com.likelion.tomorrowrich.global.security.SecurityUtil;
import com.likelion.tomorrowrich.home.dto.HomeResponseDTO;
import com.likelion.tomorrowrich.home.service.HomeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/home")
public class HomeController {

    private final HomeService homeService;
    private final SecurityUtil securityUtil;

    public HomeController(
            HomeService homeService,
            SecurityUtil securityUtil
    ) {
        this.homeService = homeService;
        this.securityUtil = securityUtil;
    }

    @GetMapping
    public ResponseEntity<HomeResponseDTO> getHome() {
        Long userId = securityUtil.getCurrentUserId();

        HomeResponseDTO response = homeService.getHome(userId);

        return ResponseEntity.ok(response);
    }
}