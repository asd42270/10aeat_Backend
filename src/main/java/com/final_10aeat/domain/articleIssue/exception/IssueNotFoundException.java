package com.final_10aeat.domain.articleIssue.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class IssueNotFoundException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ISSUE_NOT_FOUND;

    public IssueNotFoundException() {
        super(ERROR_CODE);
    }
}
