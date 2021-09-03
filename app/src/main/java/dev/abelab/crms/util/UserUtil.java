package dev.abelab.crms.util;

import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.api.response.UserResponse;

public class UserUtil {

    /**
     * フルネームを取得
     *
     * @param user ユーザ
     *
     * @return フルネーム
     */
    public static String getFullName(final User user) {
        return String.format("%s %s", user.getLastName(), user.getFirstName());
    }

}
