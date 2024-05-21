package com.final_10aeat.domain.member.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class UnauthorizedAccessException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.UNAUTHORIZED_ACCESS;

    public UnauthorizedAccessException() {
        super(ERROR_CODE);
    }

    public UnauthorizedAccessException(String message) {
        super(ERROR_CODE, message);
    }
}
