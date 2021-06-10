package dev.abelab.crs.exception;

import static org.springframework.http.HttpStatus.NOT_FOUND;

/**
 * Not found exception
 */
public class NotFoundException extends BaseException {

    /**
     * create not found exception
     */
    public NotFoundException() {
        super(NOT_FOUND);
    }

}
