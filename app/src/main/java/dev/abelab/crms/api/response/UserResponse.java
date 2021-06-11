package dev.abelab.crms.api.response;

import lombok.*;

/**
 * ユーザ情報レスポンス
 */
@Value
@Builder
@RequiredArgsConstructor
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
