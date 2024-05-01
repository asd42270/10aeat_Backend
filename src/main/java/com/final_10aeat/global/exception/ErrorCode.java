package com.final_10aeat.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    //4xx
    MEMBER_NOT_EXIST(HttpStatus.FORBIDDEN, "존재하지 않는 사용자입니다."),
    TOKEN_NOT_FOUND(HttpStatus.FORBIDDEN, "토큰을 찾을 수 없습니다."),

    // 5xx
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러");


    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
