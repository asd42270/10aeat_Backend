package com.final_10aeat.domain.articleIssue.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class InactiveIssueUpdateException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.INACTIVE_ISSUE_UPDATE_ATTEMPT;

    public InactiveIssueUpdateException() {
        super(ERROR_CODE);
    }
}
