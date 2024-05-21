package com.final_10aeat.domain.member.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class UserNotExistException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.USER_NOT_EXIST;

    public UserNotExistException() {
        super(ERROR_CODE);
    }
}
