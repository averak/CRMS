package dev.abelab.crs.api.request;

import lombok.*;

/**
 * ユーザリクエスト
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {

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
     * パスワード
     */
    String password;

    /**
     * ロールID
     */
    Integer roleId;

}
