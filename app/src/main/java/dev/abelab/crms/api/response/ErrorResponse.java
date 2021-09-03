package dev.abelab.crms.api.response;

import lombok.*;

/**
 * エラー情報レスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
