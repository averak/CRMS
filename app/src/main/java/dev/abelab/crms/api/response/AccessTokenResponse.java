package dev.abelab.crms.api.response;

import lombok.*;

/**
 * アクセストークンレスポンス
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AccessTokenResponse {

    /**
     * アクセストークン
     */
    String accessToken;

    /**
     * トークンの種類
     */
    String tokenType;

}
