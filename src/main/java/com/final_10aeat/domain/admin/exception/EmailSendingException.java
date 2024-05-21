package com.final_10aeat.domain.admin.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class EmailSendingException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.EMAIL_SENDING_FAILURE;

    public EmailSendingException() {

        super(ERROR_CODE);
    }
}
