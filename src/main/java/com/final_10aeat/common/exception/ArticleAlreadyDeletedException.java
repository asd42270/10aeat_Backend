package com.final_10aeat.common.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class ArticleAlreadyDeletedException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ARTICLE_ALREADY_DELETED;

    public ArticleAlreadyDeletedException() {

        super(ERROR_CODE);
    }
}
