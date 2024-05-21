package com.final_10aeat.domain.member.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class MemberMissMatchException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MEMBER_MISMATCH;

    public MemberMissMatchException() {
        super(ERROR_CODE);
    }
}
