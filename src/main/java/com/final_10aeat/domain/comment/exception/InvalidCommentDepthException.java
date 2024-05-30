package com.final_10aeat.domain.comment.exception;

import static com.final_10aeat.global.exception.ErrorCode.INVALID_COMMENT_DEPTH;

import com.final_10aeat.global.exception.ApplicationException;
import com.final_10aeat.global.exception.ErrorCode;

public class InvalidCommentDepthException extends ApplicationException {

    private static final ErrorCode ERROR_CODE = INVALID_COMMENT_DEPTH;

    public InvalidCommentDepthException() {
        super(ERROR_CODE);
    }
}
