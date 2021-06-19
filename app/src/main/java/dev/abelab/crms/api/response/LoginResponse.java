package dev.abelab.crms.api.response;

import lombok.*;

/**
 * ログインレスポンス
 */
@Value
@Builder
@RequiredArgsConstructor
public class LoginResponse {

    /**
     * JWT
     * */
    String jwt;

}
