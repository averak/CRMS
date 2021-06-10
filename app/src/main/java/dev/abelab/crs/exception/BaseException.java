package dev.abelab.crs.exception;

import org.springframework.http.HttpStatus;

/**
 * Base exception
 * */
public class BaseException extends RuntimeException {

    /**
     * http status
     */
    private HttpStatus httpStatus;

    /**
     * create base exception by message
     *
     * @param message
     */
    public BaseException(final String message) {
        super(message);
    }

    /**
     * create base exception by http status
     *
     * @param message
     */
    public BaseException(HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
    }

    /**
     * getter of http status
     */
    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

}
