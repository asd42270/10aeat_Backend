package com.final_10aeat.global.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public enum ErrorCode {

    // COMMON
    NOT_FOUND(HttpStatus.NOT_FOUND, "경로가 올바르지 않습니다"),
    EXPIRED_TOKEN(HttpStatus.UNAUTHORIZED, "토큰이 만료되었습니다."),
    UNAUTHORIZED_ACCESS(HttpStatus.UNAUTHORIZED, "접근 권한이 없습니다."),
    UNEXPECTED_PRINCIPAL(HttpStatus.INTERNAL_SERVER_ERROR, "예상치 못한 Principal 타입입니다."),

    // MANAGER
    MANAGER_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 관리자입니다"),
    ARTICLE_ALREADY_DELETED(HttpStatus.BAD_REQUEST, "이미 삭제된 게시글입니다."),

    // PROGRESS
    CUSTOM_PROGRESS_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 안건입니다."),

    // EMAIL
    EMAIL_SENDING_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 전송에 실패했습니다."),
    EMAIL_TEMPLATE_LOAD_FAILURE(HttpStatus.INTERNAL_SERVER_ERROR, "이메일 템플릿 로드에 실패했습니다."),
    EMAIL_VERIFICATION_CODE_EXPIRED(HttpStatus.GONE, "인증 코드의 유효 기간이 만료되었습니다."),
    INVALID_VERIFICATION_CODE(HttpStatus.BAD_REQUEST, "인증 코드가 일치하지 않습니다."),

    // OFFICE
    OFFICE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 건물입니다."),

    // BUILDING_INFO
    BUILDING_INFO_MINIMUM_ONE_REQUIRED(HttpStatus.BAD_REQUEST, "회원은 최소 하나 이상의 건물 정보를 가져야 합니다."),
    BUILDING_INFO_NOT_FOUND(HttpStatus.NOT_FOUND, "건물 정보를 찾을 수 없습니다."),
    BUILDING_INFO_NOT_ASSOCIATED_WITH_MEMBER(HttpStatus.BAD_REQUEST, "건물 정보가 회원과 연관되어 있지 않습니다."),
    DUPLICATED_BUILDING_INFO(HttpStatus.CONFLICT,"동일한 동호수가 이미 존재합니다."),

    // USER
    USER_NOT_EXIST(HttpStatus.BAD_REQUEST, "존재하지 않는 사용자입니다."),

    // MEMBER
    TOKEN_NOT_FOUND(HttpStatus.UNAUTHORIZED, "토큰을 찾을 수 없습니다."),
    DUPLICATED_EMAIL(HttpStatus.CONFLICT, "중복된 이메일입니다."),
    PASSWORD_MISMATCH(HttpStatus.BAD_REQUEST, "아이디 비밀번호가 일치하지 않습니다."),
    DISAGREE_TERM(HttpStatus.CONFLICT, "약관에 동의해야 합니다."),

    // ARTICLE
    ARTICLE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 게시글입니다."),
    ARTICLE_NOT_LIKED(HttpStatus.BAD_REQUEST, "저장하지 않은 게시글입니다."),
    ARTICLE_ALREADY_LIKED(HttpStatus.CONFLICT, "이미 저장한 게시글입니다."),

    // SCHEDULE
    SCHEDULE_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 일정입니다."),
    SCHEDULE_MUST_HAVE_ONE(HttpStatus.BAD_REQUEST, "일정은 1개 이상 존재해야 합니다."),

    // ISSUE
    ISSUE_NOT_FOUND(HttpStatus.NOT_FOUND, "이슈를 찾을 수 없습니다."),

    // COMMENT
    INVALID_COMMENT_DEPTH(HttpStatus.BAD_REQUEST, "대댓글은 1단만 작성할 수 있습니다."),
    PARENT_COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "부모 댓글을 찾을 수 없습니다."),
    COMMENT_NOT_FOUND(HttpStatus.NOT_FOUND, "존재하지 않는 댓글입니다."),

    // 5xx
    INTERNAL_SERVER_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "서버 내부 에러");

    private final HttpStatus httpStatus;
    private final String message;

    ErrorCode(HttpStatus httpStatus, String message) {
        this.httpStatus = httpStatus;
        this.message = message;
    }
}