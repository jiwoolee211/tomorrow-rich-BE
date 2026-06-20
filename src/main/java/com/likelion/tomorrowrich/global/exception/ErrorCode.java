package com.likelion.tomorrowrich.global.exception;

import org.springframework.http.HttpStatus;

public enum ErrorCode {

    INVALID_REQUEST(
            HttpStatus.BAD_REQUEST,
            "INVALID_REQUEST",
            "잘못된 요청",
            "잘못된 요청입니다."
    ),

    UNAUTHORIZED(
            HttpStatus.UNAUTHORIZED,
            "UNAUTHORIZED",
            "인증 실패",
            "인증이 필요합니다."
    ),

    FORBIDDEN(
            HttpStatus.FORBIDDEN,
            "FORBIDDEN",
            "접근 권한 없음",
            "접근 권한이 없습니다."
    ),

    RESOURCE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "RESOURCE_NOT_FOUND",
            "리소스 없음",
            "요청한 리소스를 찾을 수 없습니다."
    ),

    CONFLICT(
            HttpStatus.CONFLICT,
            "CONFLICT",
            "요청 충돌",
            "이미 처리된 요청입니다."
    ),

    INTERNAL_SERVER_ERROR(
            HttpStatus.INTERNAL_SERVER_ERROR,
            "INTERNAL_SERVER_ERROR",
            "서버 오류",
            "서버 오류가 발생했습니다."
    ),

    DUPLICATED_LOGIN_ID(
            HttpStatus.CONFLICT,
            "DUPLICATED_LOGIN_ID",
            "이미 사용 중인 아이디",
            "이미 사용 중인 아이디입니다."
    ),

    PASSWORD_MISMATCH(
            HttpStatus.BAD_REQUEST,
            "PASSWORD_MISMATCH",
            "비밀번호 불일치",
            "비밀번호가 일치하지 않습니다."
    ),

    MEMBER_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "MEMBER_NOT_FOUND",
            "사용자 찾을 수 없음",
            "존재하지 않는 계정입니다."
    ),

    INVALID_PASSWORD(
            HttpStatus.UNAUTHORIZED,
            "INVALID_PASSWORD",
            "비밀번호 불일치",
            "비밀번호가 일치하지 않습니다."
    ),

    EMPTY_LOGIN_ID(
            HttpStatus.BAD_REQUEST,
            "EMPTY_LOGIN_ID",
            "아이디 미입력",
            "아이디를 입력해 주세요."
    ),

    EMPTY_NICKNAME(
            HttpStatus.BAD_REQUEST,
            "EMPTY_NICKNAME",
            "닉네임 미입력",
            "닉네임을 입력해 주세요."
    ),

    EMPTY_CHARACTER_NAME(
            HttpStatus.BAD_REQUEST,
            "EMPTY_CHARACTER_NAME",
            "말랑이 이름 미입력",
            "말랑이 이름을 입력해 주세요."
    ),

    INVALID_BUDGET(
            HttpStatus.BAD_REQUEST,
            "INVALID_BUDGET",
            "잘못된 예산",
            "올바른 예산을 입력해 주세요."
    ),

    ALREADY_ONBOARDED(
            HttpStatus.CONFLICT,
            "ALREADY_ONBOARDED",
            "이미 완료된 온보딩",
            "이미 온보딩을 완료했습니다."
    ),

    INVALID_EXPENSE_AMOUNT(
            HttpStatus.BAD_REQUEST,
            "INVALID_EXPENSE_AMOUNT",
            "잘못된 소비 금액",
            "올바른 금액을 입력해 주세요."
    ),

    CATEGORY_REQUIRED(
            HttpStatus.BAD_REQUEST,
            "CATEGORY_REQUIRED",
            "카테고리 필수",
            "카테고리를 선택해 주세요."
    ),

    FUTURE_DATE_NOT_ALLOWED(
            HttpStatus.BAD_REQUEST,
            "FUTURE_DATE_NOT_ALLOWED",
            "미래 날짜 불가",
            "아직 기록할 수 없는 날짜입니다."
    ),

    EXPENSE_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "EXPENSE_NOT_FOUND",
            "소비 기록 없음",
            "소비 기록을 찾을 수 없습니다."
    ),

    MISSION_NOT_FOUND(
            HttpStatus.NOT_FOUND,
            "MISSION_NOT_FOUND",
            "미션 없음",
            "미션을 찾을 수 없습니다."
    ),

    MISSION_NOT_COMPLETABLE(
            HttpStatus.BAD_REQUEST,
            "MISSION_NOT_COMPLETABLE",
            "완료할 수 없는 미션",
            "아직 미션 조건을 만족하지 못했어요."
    ),

    MISSION_ALREADY_COMPLETED(
            HttpStatus.CONFLICT,
            "MISSION_ALREADY_COMPLETED",
            "이미 완료한 미션",
            "이미 완료한 미션입니다."
    ),

    FUTURE_MISSION_NOT_ALLOWED(
            HttpStatus.BAD_REQUEST,
            "FUTURE_MISSION_NOT_ALLOWED",
            "미래 미션 완료 불가",
            "아직 완료할 수 없는 미션입니다."
    ),

    INVALID_ITEM_TYPE(
            HttpStatus.BAD_REQUEST,
            "INVALID_ITEM_TYPE",
            "잘못된 아이템 타입",
            "아이템 타입이 올바르지 않습니다."
    );

    private final HttpStatus status;
    private final String code;
    private final String error;
    private final String message;

    ErrorCode(HttpStatus status, String code, String error, String message) {
        this.status = status;
        this.code = code;
        this.error = error;
        this.message = message;
    }

    public HttpStatus getStatus() {
        return status;
    }

    public String getCode() {
        return code;
    }

    public String getError() {
        return error;
    }

    public String getMessage() {
        return message;
    }
}