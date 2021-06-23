package dev.abelab.crms.api.request;

import java.util.Date;

import lombok.*;

/**
 * ユーザ作成リクエスト
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserCreateRequest {

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
