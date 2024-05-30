package com.final_10aeat.domain.member.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class PasswordMissMatchException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.PASSWORD_MISMATCH;

    public PasswordMissMatchException() {
        super(ERROR_CODE);
    }

    public PasswordMissMatchException(String message) {
        super(ERROR_CODE, message);
    }
}
