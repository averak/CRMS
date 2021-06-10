package dev.abelab.crms.api.response;

import java.util.List;
import lombok.Value;

/**
 * ユーザ一覧レスポンス
 */
@Value
public class UsersResponse {

    /**
     * ユーザリスト
     */
    List<UserResponse> users;

}
