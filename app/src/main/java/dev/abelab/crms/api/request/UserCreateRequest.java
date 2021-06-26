package dev.abelab.crms.api.request;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

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
     * パスワード
     */
    @NotNull
    @Size(min = 8, max = 255)
    String password;

    /**
     * ロールID
     */
    @NotNull
    Integer roleId;

    /**
     * 入学年度
     */
    @NotNull
    Integer admissionYear;

}
