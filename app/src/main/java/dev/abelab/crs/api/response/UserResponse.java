package dev.abelab.crs.api.response;

import lombok.Value;

/**
 * ユーザ情報レスポンス
 */
@Value
public class UserResponse {

    /**
     * ユーザID
     */
    Integer id;

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
     * ロールID
     */
    Integer roleId;

}
