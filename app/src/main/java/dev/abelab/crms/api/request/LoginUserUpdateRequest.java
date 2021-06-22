package dev.abelab.crms.api.request;

import lombok.*;

/**
 * ログインユーザ更新リクエスト
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserUpdateRequest {

    /**
     * ファーストネーム
     */
    String firstName;

    /**
     * ラストネーム
     */
    String lastName;

    /**
     * メールアドレス
     */
    String email;

    /**
     * 現在のパスワード
     */
    String currentPassword;

    /**
     * 新しいパスワード
     */
    String newPassword;

}
