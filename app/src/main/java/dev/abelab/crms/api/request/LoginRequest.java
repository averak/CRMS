package dev.abelab.crms.api.request;

import javax.validation.constraints.NotNull;

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
    @NotNull
    String email;

    /**
     * パスワード
     */
    @NotNull
    String password;

}
