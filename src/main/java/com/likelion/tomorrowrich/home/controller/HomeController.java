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
         * 현재는 임시 사용자 id임
         */
        return 1L;
    }
}