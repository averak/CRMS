package dev.abelab.crms.api.request;

import lombok.*;

/**
 * ログインリクエスト
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginRequest {

    /**
     * メールアドレス
     */
    String email;

    /**
     * パスワード
     */
    String password;

}
