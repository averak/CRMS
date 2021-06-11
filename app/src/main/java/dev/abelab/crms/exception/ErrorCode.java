package dev.abelab.crms.exception;

import java.util.Arrays;

import lombok.*;

/**
 * The enum error code
 */
@Getter
@AllArgsConstructor
public enum ErrorCode {

    /**
     * Internal Server Error: 1000~1099
     */
    UNEXPECTED_ERROR(1000, "exception.internal_server_error.unexpected_error"),

    /**
     * Not Found: 1100~1199
     */
    NOT_FOUND_ERROR_CODE(1100, "exception.not_found.error_code"),

    NOT_FOUND_USER(1101, "exception.not_found.user"),

    NOT_FOUND_ROLE(1102, "exception.not_found.role"),

    /**
     * Conflict: 1200~1299
     */
    CONFLICT_EMAIL(1200, "exception.conflict.email"),

    /**
     * Forbidden: 1300~1399
     */
    USER_HAS_NO_PERMISSION(1300, "exception.forbidden.user_has_no_permission");

    private final int code;

    private final String messageKey;

    /**
     * find by code
     *
     * @param code code
     *
     * @return error code
     */
    public static ErrorCode findById(final int code) {
        return Arrays.stream(values()).filter(e -> e.getCode() == code) //
            .findFirst().orElseThrow(() -> new NotFoundException(NOT_FOUND_ERROR_CODE));
    }

}
