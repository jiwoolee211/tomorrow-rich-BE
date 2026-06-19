package com.likelion.tomorrowrich.home.controller;

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

    public HomeController(HomeService homeService) {
        this.homeService = homeService;
    }

    @GetMapping
    public ResponseEntity<HomeResponseDTO> getHome() {
        Long userId = getLoginUserId();

        HomeResponseDTO response = homeService.getHome(userId);

        return ResponseEntity.ok(response);
    }

    private Long getLoginUserId() {
        /*
         * TODO:
         * 인증 기능이 완성되면 SecurityContext 또는 @AuthenticationPrincipal에서
         * 로그인한 사용자의 id를 꺼내도록 변경해야 합니다.
         *
         * 현재는 인증 모듈 연결 전 개발/테스트용 임시 사용자 id입니다.
         */
        return 1L;
    }
}