package com.final_10aeat.domain.manageArticle.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class ScheduleMustHaveOneException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.SCHEDULE_MUST_HAVE_ONE;

    public ScheduleMustHaveOneException() {

        super(ERROR_CODE);
    }
}
