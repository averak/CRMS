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

    FAILED_TO_SEND_SLACK(1001, "exception.internal_server_error.failed_to_send_slack"),

    /**
     * Not Found: 1100~1199
     */
    NOT_FOUND_ERROR_CODE(1100, "exception.not_found.error_code"),

    NOT_FOUND_USER(1101, "exception.not_found.user"),

    NOT_FOUND_ROLE(1102, "exception.not_found.role"),

    NOT_FOUND_RESERVATION(1103, "exception.not_found.reservation"),

    NOT_FOUND_API(1104, "exception.not_found.api"),

    /**
     * Conflict: 1200~1299
     */
    CONFLICT_EMAIL(1200, "exception.conflict.email"),

    CONFLICT_RESERVATION_TIME(1201, "exception.conflict.reservation"),

    /**
     * Forbidden: 1300~1399
     */
    USER_HAS_NO_PERMISSION(1300, "exception.forbidden.user_has_no_permission"),

    /**
     * Bad Request: 1400~1499
     */
    VALIDATION_ERROR(1400, "exception.bad_request.validation_error"),

    INVALID_RESERVATION(1401, "exception.bad_request.invalid_reservation"),

    TOO_LONG_RESERVATION_HOURS(1402, "exception.bad_request.too_long_reservation_hours"),

    PAST_RESERVATION_CANNOT_BE_MODIFIED(1403, "exception.bad_request.past_reservation_cannot_be_modified"),

    PAST_RESERVATION_CANNOT_BE_DELETED(1404, "exception.bad_request.past_reservation_cannot_be_deleted"),

    TOO_SHORT_PASSWORD(1405, "exception.bad_request.too_short_password"),

    /**
     * Unauthorized: 1500~1599
     */
    USER_NOT_LOGGED_IN(1500, "exception.unauthorized.user_not_logged_in"),

    WRONG_PASSWORD(1501, "exception.unauthorized.wrong_password"),

    INVALID_ACCESS_TOKEN(1502, "exception.unauthorized.invalid_access_token"),

    EXPIRED_ACCESS_TOKEN(1503, "exception.unauthorized.expired_access_token");

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
