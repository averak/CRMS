package dev.abelab.crms.api.response;

import lombok.*;

@Value
@Builder
@AllArgsConstructor
public class ErrorResponse {

    /**
     * error message
     */
    String message;

    /**
     * error code
     */
    int code;

}
