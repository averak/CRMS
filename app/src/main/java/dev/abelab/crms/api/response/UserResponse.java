package dev.abelab.crms.api.response;

import java.util.Date;

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

    /**
     * 入学年度
     */
    Integer admissionYear;

}
