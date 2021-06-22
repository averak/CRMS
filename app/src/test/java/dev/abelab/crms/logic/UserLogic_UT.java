package dev.abelab.crms.logic;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.mockito.ArgumentMatchers.anyInt;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import org.springframework.security.crypto.password.PasswordEncoder;

import dev.abelab.crms.db.entity.UserSample;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.property.JwtProperty;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.ForbiddenException;

public class UserLogic_UT extends AbstractLogic_UT {

    @Injectable
    UserRepository userRepository;

    @Injectable
    JwtProperty jwtProperty;

    @Injectable
    PasswordEncoder passwordEncoder;

    @Tested
    UserLogic userLogic;

    /**
     * Test for check admin
     */
    @Nested
    @TestInstance(PER_CLASS)
    class CheckAdminTest {

        @Test
        void 正_管理人である() {
            // setup
            final var user = UserSample.builder().roleId(UserRoleEnum.ADMIN.getId()).build();

            new Expectations() {
                {
                    userRepository.selectById(anyInt);
                    result = user;
                }
            };

            // verify
            assertDoesNotThrow(() -> userLogic.checkAdmin(anyInt()));
        }

        @Test
        void 異_管理人ではない() {
            // setup
            final var user = UserSample.builder().roleId(UserRoleEnum.MEMBER.getId()).build();

            new Expectations() {
                {
                    userRepository.selectById(anyInt);
                    result = user;
                }
            };

            // verify
            final var exception = assertThrows(ForbiddenException.class, () -> userLogic.checkAdmin(anyInt()));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_HAS_NO_PERMISSION);
        }

    }

}
