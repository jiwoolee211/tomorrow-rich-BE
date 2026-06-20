package com.likelion.tomorrowrich.global.security;

import com.likelion.tomorrowrich.global.exception.BusinessException;
import com.likelion.tomorrowrich.global.exception.ErrorCode;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtil {

    public Long getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication == null || authentication.getPrincipal() == null) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        Object principal = authentication.getPrincipal();

        if (!(principal instanceof CustomUserDetails userDetails)) {
            throw new BusinessException(ErrorCode.UNAUTHORIZED);
        }

        return userDetails.getUserId();
    }
}