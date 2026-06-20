package com.likelion.tomorrowrich.global.security;

import com.likelion.tomorrowrich.global.exception.BusinessException;
import com.likelion.tomorrowrich.global.exception.ErrorCode;
import com.likelion.tomorrowrich.user.entity.User;
import com.likelion.tomorrowrich.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String loginId) {
        User user = userRepository.findByLoginId(loginId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        return new CustomUserDetails(user);
    }

    public CustomUserDetails loadUserById(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new BusinessException(ErrorCode.UNAUTHORIZED));

        return new CustomUserDetails(user);
    }
}
