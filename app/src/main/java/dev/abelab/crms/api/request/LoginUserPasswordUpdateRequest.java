package dev.abelab.crms.api.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import lombok.*;

/**
 * ログインユーザのパスワード更新リクエスト
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginUserPasswordUpdateRequest {

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
