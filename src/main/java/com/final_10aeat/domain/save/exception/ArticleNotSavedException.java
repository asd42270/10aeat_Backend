package com.final_10aeat.domain.save.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class ArticleNotSavedException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ARTICLE_NOT_SAVED;

    public ArticleNotSavedException() {

        super(ERROR_CODE);
    }
}
