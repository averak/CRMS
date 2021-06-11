package dev.abelab.crms.logic;

import org.springframework.stereotype.Component;

import lombok.*;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.ForbiddenException;

@RequiredArgsConstructor
@Component
public class UserLogic {

    private final UserRepository userRepository;

    /**
     * 管理者チェック
     *
     * @param userId ユーザID
     *
     * @return 管理者かどうか
     */
    public void checkAdmin(final int userId) {
        final var user = this.userRepository.selectById(userId);

        if (user.getRoleId() != UserRoleEnum.ADMIN.getId()) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }
    }
}
