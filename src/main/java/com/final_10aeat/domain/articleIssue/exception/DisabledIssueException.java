package com.final_10aeat.domain.articleIssue.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class DisabledIssueException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.INACTIVE_ISSUE_ATTEMPT;

    public DisabledIssueException() {
        super(ERROR_CODE);
    }
}
