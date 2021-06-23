package dev.abelab.crms.util;

import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.api.response.UserResponse;

public class UserUtil {

    /**
     * ユーザレスポンスを作成
     *
     * @param user ユーザ
     *
     * @return ユーザレスポンス
     */
    public static UserResponse buildUserResponse(final User user) {
        return UserResponse.builder() //
            .id(user.getId()) //
            .firstName(user.getFirstName()) //
            .lastName(user.getLastName()) //
            .email(user.getEmail()) //
            .roleId(user.getRoleId()) //
            .admissionYear(user.getAdmissionYear()) //
            .build();
    }

}
