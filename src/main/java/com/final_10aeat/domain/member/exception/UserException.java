package com.final_10aeat.domain.member.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class UserException  extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MEMBER_NOT_EXIST;

    public UserException() {
        super(ERROR_CODE);
    }

}
