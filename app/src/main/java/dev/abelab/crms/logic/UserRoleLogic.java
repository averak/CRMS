package dev.abelab.crms.logic;

import org.springframework.stereotype.Component;

import lombok.*;
import dev.abelab.crms.enums.UserRoleEnum;

@RequiredArgsConstructor
@Component
public class UserRoleLogic {

    /**
     * ユーザロールのチェック
     *
     * @param roleId ロールID
     */
    public void checkForValidRoleId(final int roleId) {
        UserRoleEnum.findById(roleId);
    }
}
