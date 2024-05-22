package com.final_10aeat.domain.repairArticle.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class ArticleNotFoundException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ARTICLE_NOT_FOUND;

    public ArticleNotFoundException() {

        super(ERROR_CODE);
    }
}
