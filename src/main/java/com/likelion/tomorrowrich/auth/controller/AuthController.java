package com.likelion.tomorrowrich.auth.controller;

import com.likelion.tomorrowrich.auth.dto.CheckLoginIdResponseDTO;
import com.likelion.tomorrowrich.auth.dto.JwtTokenResponseDTO;
import com.likelion.tomorrowrich.auth.dto.LoginRequestDTO;
import com.likelion.tomorrowrich.auth.dto.SignupRequestDTO;
import com.likelion.tomorrowrich.auth.dto.SignupResponseDTO;
import com.likelion.tomorrowrich.auth.service.AuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ResponseEntity<SignupResponseDTO> signup(
            @RequestBody SignupRequestDTO request
    ) {
        SignupResponseDTO response = authService.signup(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtTokenResponseDTO> login(
            @RequestBody LoginRequestDTO request
    ) {
        JwtTokenResponseDTO response = authService.login(request);

        return ResponseEntity.ok(response);
    }

    @PostMapping("/logout")
    public ResponseEntity<String> logout() {
        return ResponseEntity.ok("로그아웃 성공");
    }

    @GetMapping("/check-login-id")
    public ResponseEntity<CheckLoginIdResponseDTO> checkLoginId(
            @RequestParam(required = false) String loginId
    ) {
        CheckLoginIdResponseDTO response = authService.checkLoginId(loginId);

        return ResponseEntity.ok(response);
    }
}
