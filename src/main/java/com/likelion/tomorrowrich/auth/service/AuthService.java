package com.likelion.tomorrowrich.auth.service;

import com.likelion.tomorrowrich.auth.dto.CheckLoginIdResponseDTO;
import com.likelion.tomorrowrich.auth.dto.JwtTokenResponseDTO;
import com.likelion.tomorrowrich.auth.dto.LoginRequestDTO;
import com.likelion.tomorrowrich.auth.dto.SignupRequestDTO;
import com.likelion.tomorrowrich.auth.dto.SignupResponseDTO;
import com.likelion.tomorrowrich.global.exception.BusinessException;
import com.likelion.tomorrowrich.global.exception.ErrorCode;
import com.likelion.tomorrowrich.global.security.JwtTokenProvider;
import com.likelion.tomorrowrich.room.entity.Room;
import com.likelion.tomorrowrich.room.repository.RoomRepository;
import com.likelion.tomorrowrich.user.entity.User;
import com.likelion.tomorrowrich.user.repository.UserRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class AuthService {

    private final UserRepository userRepository;
    private final RoomRepository roomRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;

    public AuthService(
            UserRepository userRepository,
            RoomRepository roomRepository,
            PasswordEncoder passwordEncoder,
            JwtTokenProvider jwtTokenProvider
    ) {
        this.userRepository = userRepository;
        this.roomRepository = roomRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    public SignupResponseDTO signup(SignupRequestDTO request) {
        validateSignupRequest(request);

        if (userRepository.existsByLoginId(request.loginId())) {
            throw new BusinessException(ErrorCode.DUPLICATED_LOGIN_ID);
        }

        if (!request.password().equals(request.passwordConfirm())) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }

        User user = new User(
                request.loginId(),
                passwordEncoder.encode(request.password())
        );

        User savedUser = userRepository.save(user);

        roomRepository.save(new Room(savedUser));

        return new SignupResponseDTO(
                savedUser.getId(),
                savedUser.getLoginId(),
                savedUser.getOnboardingCompleted()
        );
    }

    @Transactional(readOnly = true)
    public JwtTokenResponseDTO login(LoginRequestDTO request) {
        validateLoginRequest(request);

        User user = userRepository.findByLoginId(request.loginId())
                .orElseThrow(() -> new BusinessException(ErrorCode.MEMBER_NOT_FOUND));

        if (!passwordEncoder.matches(request.password(), user.getPassword())) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }

        String accessToken = jwtTokenProvider.createAccessToken(user.getId());

        return new JwtTokenResponseDTO(
                accessToken,
                user.getId(),
                defaultString(user.getNickname()),
                user.getOnboardingCompleted()
        );
    }

    @Transactional(readOnly = true)
    public CheckLoginIdResponseDTO checkLoginId(String loginId) {
        if (loginId == null || loginId.isBlank()) {
            throw new BusinessException(ErrorCode.EMPTY_LOGIN_ID);
        }

        boolean available = !userRepository.existsByLoginId(loginId);

        return new CheckLoginIdResponseDTO(available);
    }

    private void validateSignupRequest(SignupRequestDTO request) {
        if (request.loginId() == null || request.loginId().isBlank()) {
            throw new BusinessException(ErrorCode.EMPTY_LOGIN_ID);
        }

        if (request.password() == null || request.password().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_REQUEST);
        }

        if (request.passwordConfirm() == null || request.passwordConfirm().isBlank()) {
            throw new BusinessException(ErrorCode.PASSWORD_MISMATCH);
        }
    }

    private void validateLoginRequest(LoginRequestDTO request) {
        if (request.loginId() == null || request.loginId().isBlank()) {
            throw new BusinessException(ErrorCode.MEMBER_NOT_FOUND);
        }

        if (request.password() == null || request.password().isBlank()) {
            throw new BusinessException(ErrorCode.INVALID_PASSWORD);
        }
    }

    private String defaultString(String value) {
        return value == null ? "" : value;
    }
}