package dev.abelab.crms.api.controller;

import java.util.Locale;

import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import lombok.*;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.BaseException;
import dev.abelab.crms.api.response.ErrorResponse;

/**
 * Rest controller exception handler
 */
@Controller
@RestControllerAdvice
@RequiredArgsConstructor
public class RestControllerExceptionHandler extends ResponseEntityExceptionHandler {

    private final MessageSource messageSource;

    private String getErrorMessage(final BaseException exception) {
        final var messageKey = exception.getErrorCode().getMessageKey();
        final var args = exception.getArgs();
        return this.messageSource.getMessage(messageKey, args, Locale.ENGLISH);
    }

    /**
     * Handle unexpected exception
     *
     * @param exception exception
     *
     * @return response entity
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleException(final Exception exception) {
        final var errorCode = ErrorCode.UNEXPECTED_ERROR;
        final var message = messageSource.getMessage(errorCode.getMessageKey(), null, Locale.ENGLISH);
        final var errorResponse = ErrorResponse.builder().message(message).code(errorCode.getCode()).build();

        return new ResponseEntity<>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    /**
     * Handle base exception
     *
     * @param exception exception
     *
     * @return response entity
     */
    @ExceptionHandler(BaseException.class)
    public ResponseEntity<ErrorResponse> handleBaseException(final BaseException exception) {
        final var message = this.getErrorMessage(exception);
        final var errorCode = exception.getErrorCode();
        final var errorResponse = ErrorResponse.builder().message(message).code(errorCode.getCode()).build();

        return new ResponseEntity<>(errorResponse, exception.getHttpStatus());
    }

}
