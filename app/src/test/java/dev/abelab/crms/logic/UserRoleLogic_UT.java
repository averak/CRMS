package dev.abelab.crms.logic;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import mockit.Tested;

import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.NotFoundException;

public class UserRoleLogic_UT extends AbstractLogic_UT {

    @Tested
    UserRoleLogic userRoleLogic;

    /**
     * Test for check for valid roleId
     */
    @Nested
    @TestInstance(PER_CLASS)
    class CheckForValidRoleIdTest {

        @ParameterizedTest
        @MethodSource
        void 正_有効なロールを付与(final int roleId) {
            // verify
            assertThatCode(() -> {
                userRoleLogic.checkForValidRoleId(roleId);
            }).doesNotThrowAnyException();
        }

        Stream<Arguments> 正_有効なロールを付与() {
            return Stream.of( // ロールID
                // 管理者
                arguments(UserRoleEnum.ADMIN.getId()),
                // メンバー
                arguments(UserRoleEnum.MEMBER.getId()));
        }

        @ParameterizedTest
        @MethodSource
        void 異_無効なロールを付与(final int roleId) {
            // verify
            final var exception = assertThrows(NotFoundException.class, () -> userRoleLogic.checkForValidRoleId(roleId));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_ROLE);
        }

        Stream<Arguments> 異_無効なロールを付与() {
            return Stream.of( // ロールID
                arguments(0), //
                arguments(UserRoleEnum.values().length + 1));
        }

    }

}
