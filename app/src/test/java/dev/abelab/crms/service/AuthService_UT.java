package dev.abelab.crms.service;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;
import static org.mockito.ArgumentMatchers.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;

import dev.abelab.crms.db.entity.UserSample;
import dev.abelab.crms.api.request.LoginRequest;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.logic.UserLogic;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.NotFoundException;
import dev.abelab.crms.exception.UnauthorizedException;

/**
 * AuthService Unit Test
 */
class AuthService_UT extends AbstractService_UT {

    @Injectable
    UserLogic userLogic;

    @Injectable
    UserRepository userRepository;

    @Tested
    AuthService authService;

    /**
     * Test for login
     */
    @Nested
    @TestInstance(PER_CLASS)
    class LoginTest {

        @ParameterizedTest
        @MethodSource
        void 正_ユーザがログイン(final UserRoleEnum userRole) {
            // setup
            final var user = UserSample.builder().roleId(userRole.getId()).build();
            final var requestBody = LoginRequest.builder() //
                .email(user.getEmail()) //
                .password(user.getPassword()) //
                .build();

            new Expectations() {
                {
                    userRepository.selectByEmail(anyString);
                    result = user;
                }
                {
                    userLogic.verifyPassword(user, anyString);
                    result = null;
                }
            };

            // verify
            assertDoesNotThrow(() -> authService.login(requestBody));

        }

        Stream<Arguments> 正_ユーザがログイン() {
            return Stream.of(
                // 管理者
                arguments(UserRoleEnum.ADMIN),
                // 一般
                arguments(UserRoleEnum.MEMBER));
        }

        @Test
        void 異_メールアドレスが存在しない() {
            // setup
            final var user = UserSample.builder().roleId(UserRoleEnum.MEMBER.getId()).build();
            final var requestBody = LoginRequest.builder() //
                .email(user.getEmail()) //
                .password(user.getPassword()) //
                .build();

            new Expectations() {
                {
                    userRepository.selectByEmail(anyString);
                    result = new NotFoundException(ErrorCode.NOT_FOUND_USER);
                }
            };

            // verify
            final var exception = assertThrows(NotFoundException.class, () -> authService.login(requestBody));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_USER);

        }

        @Test
        void 異_パスワードが間違っている() {
            // setup
            final var user = UserSample.builder().roleId(UserRoleEnum.MEMBER.getId()).build();
            final var requestBody = LoginRequest.builder() //
                .email(user.getEmail()) //
                .password(user.getPassword()) //
                .build();

            new Expectations() {
                {
                    userRepository.selectByEmail(anyString);
                    result = user;
                }
                {
                    userLogic.verifyPassword(user, anyString);
                    result = new UnauthorizedException(ErrorCode.WRONG_PASSWORD);
                }
            };

            // verify
            final var exception = assertThrows(UnauthorizedException.class, () -> authService.login(requestBody));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.WRONG_PASSWORD);
        }

    }
}
