package com.final_10aeat.domain.member.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class MemberDuplicatedException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.DUPLICATED_EMAIL;

    public MemberDuplicatedException() {
        super(ERROR_CODE);
    }
}
