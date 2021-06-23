package dev.abelab.crms.api.request;

import java.util.Date;

import lombok.*;

/**
 * ユーザ更新リクエスト
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserUpdateRequest {

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

    /**
     * 入学年度
     */
    Integer admissionYear;

}
