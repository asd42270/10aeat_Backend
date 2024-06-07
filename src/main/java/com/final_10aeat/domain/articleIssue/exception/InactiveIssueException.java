package com.final_10aeat.domain.articleIssue.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class InactiveIssueException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.INACTIVE_ISSUE_ATTEMPT;

    public InactiveIssueException() {
        super(ERROR_CODE);
    }
}
