package dev.abelab.crms.logic;

import org.springframework.stereotype.Component;
import org.springframework.security.crypto.password.PasswordEncoder;

import lombok.*;
import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.util.AuthUtil;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.UnauthorizedException;
import dev.abelab.crms.exception.ForbiddenException;

@RequiredArgsConstructor
@Component
public class UserLogic {

    private final UserRepository userRepository;

    private final PasswordEncoder passwordEncoder;

    /**
     * 管理者チェック
     *
     * @param userId ユーザID
     */
    public void checkAdmin(final int userId) {
        final var user = this.userRepository.selectById(userId);

        if (user.getRoleId() != UserRoleEnum.ADMIN.getId()) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }
    }

    /**
     * ログインユーザを取得
     *
     * @param jwt JWT
     *
     * @return ユーザ
     */
    public User getLoginUser(final String jwt) {
        // JWTの有効性を検証
        AuthUtil.verifyJwt(jwt);

        final int userId = AuthUtil.getUserIdFromJwt(jwt);
        return this.userRepository.selectById(userId);
    }

    /**
     * パスワードをハッシュ化
     *
     * @param password パスワード
     *
     * @return ハッシュ値
     */
    public String encodePassword(final String password) {
        return this.passwordEncoder.encode(password);
    }

    /**
     * パスワードが一致するか検証
     *
     * @param userId   ユーザID
     *
     * @param password パスワード
     */
    public void verifyPassword(final int userId, final String password) {
        final var user = this.userRepository.selectById(userId);

        // ハッシュ値が一致するか
        if (!this.passwordEncoder.matches(password, user.getPassword())) {
            throw new UnauthorizedException(ErrorCode.WRONG_PASSWORD);
        }
    }
}
