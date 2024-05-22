package com.final_10aeat.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // COMMON
    UNAUTHORIZED_ACCESS(HttpStatus.FORBIDDEN, "접근 권한이 없습니다."),

    // MANAGER
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 관리자입니다"),

    // ARTICLE
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 게시글입니다."),

    // EMAIL
    EMAIL_SENDING_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다."),
    EMAIL_TEMPLATE_LOAD_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 템플릿 로드에 실패했습니다."),
    EMAIL_VERIFICATION_CODE_EXPIRED(HttpStatus.GONE, "인증 코드의 유효 기간이 만료되었습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),

    // OFFICE
    OFFICE_NOT_FOUND(HttpStatus.NOT_FOUND,"존재하지 않는 건물입니다."),

    // USER
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),

    // MEMBER
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰을 찾을 수 없습니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "중복된 이메일입니다."),
    MEMBER_MISMATCH(HttpStatus.CONFLICT, "일치하지 않는 사용자입니다."),
    DISAGREE_TERM(HttpStatus.CONFLICT, "약관에 동의해야 합니다."),

    // 5xx
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}
