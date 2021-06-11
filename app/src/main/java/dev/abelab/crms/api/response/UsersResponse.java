package dev.abelab.crms.api.response;

import java.util.List;

import lombok.*;

/**
 * ユーザ一覧レスポンス
 */
@Value
@Builder
@RequiredArgsConstructor
public class UsersResponse {

    /**
     * ユーザリスト
     */
    List<UserResponse> users;

}
