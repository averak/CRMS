package dev.abelab.crms.api.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
    @NotNull
    String firstName;

    /**
     * ラストネーム
     */
    @NotNull
    String lastName;

    /**
     * メールアドレス
     */
    @NotNull
    String email;

    /**
     * 現在のパスワード
     */
    @NotNull
    @Size(min = 8, max = 255)
    String currentPassword;

    /**
     * 新しいパスワード
     */
    @NotNull
    @Size(min = 8, max = 255)
    String newPassword;

}
