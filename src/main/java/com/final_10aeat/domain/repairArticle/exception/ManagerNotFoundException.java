package com.final_10aeat.domain.repairArticle.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class ManagerNotFoundException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.MANAGER_NOT_FOUND;

    public ManagerNotFoundException() {

        super(ERROR_CODE);
    }
}
