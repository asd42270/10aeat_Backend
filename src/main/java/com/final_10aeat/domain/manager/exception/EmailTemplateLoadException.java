package com.final_10aeat.domain.manager.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class EmailTemplateLoadException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.EMAIL_TEMPLATE_LOAD_FAILURE;

    public EmailTemplateLoadException() {

        super(ERROR_CODE);
    }
}
