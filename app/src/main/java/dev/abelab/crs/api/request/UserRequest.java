package dev.abelab.crs.api.request;

import java.util.List;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

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
     * パスワード
     */
    String password;

    /**
     * ロールID
     */
    Integer roleId;

}
