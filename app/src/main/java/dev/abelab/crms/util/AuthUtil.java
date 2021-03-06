package dev.abelab.crms.util;

import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.ForbiddenException;
import dev.abelab.crms.exception.BadRequestException;

public class AuthUtil {

    /**
     * 管理者かどうかチェック
     *
     * @param user ユーザ
     */
    public static void checkAdmin(final User user) {
        if (user.getRoleId() != UserRoleEnum.ADMIN.getId()) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }
    }

    /**
     * 有効なパスワードがチェック
     */
    public static void validatePassword(final String password) {
        // 8~32文字かどうか
        if (password.length() < 8 || password.length() > 32) {
            throw new BadRequestException(ErrorCode.INVALID_PASSWORD_SIZE);
        }
        // 大文字・小文字・数字を含むか
        if (!password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9]).+$")) {
            throw new BadRequestException(ErrorCode.TOO_SIMPLE_PASSWORD);
        }
    }

}
