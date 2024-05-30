package com.final_10aeat.domain.save.exception;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class ArticleAlreadyLikedException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = ErrorCode.ARTICLE_ALREADY_LIKED;

    public ArticleAlreadyLikedException() {

        super(ERROR_CODE);
    }
}
