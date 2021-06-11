package dev.abelab.crms.exception;

import static org.springframework.http.HttpStatus.FORBIDDEN;;

/**
 * Forbidden exception
 */
public class ForbiddenException extends BaseException {

    /**
     * create not found exception
     *
     * @param ErrorCode error code
     */
    public ForbiddenException(final ErrorCode errorCode) {
        super(FORBIDDEN, errorCode);
    }

}
