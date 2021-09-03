package dev.abelab.crms.api.controller.internal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpHeaders;

import dev.abelab.crms.api.controller.AbstractRestController_IT;
import dev.abelab.crms.db.entity.UserSample;
import dev.abelab.crms.api.request.LoginRequest;
import dev.abelab.crms.api.response.AccessTokenResponse;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.NotFoundException;
import dev.abelab.crms.exception.UnauthorizedException;

/**
 * AuthRestController Integration Test
 */
public class AuthRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api";
	static final String LOGIN_PATH = BASE_PATH + "/login";

	/**
	 * ログインAPIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class LoginTest extends AbstractRestControllerInitialization_IT {

		@ParameterizedTest
		@MethodSource
		void 正_存在するユーザがログイン(final UserRoleEnum userRole) throws Exception {
			// setup
			createLoginUser(userRole);

			// login request body
			final var requestBody = LoginRequest.builder() //
				.email(LOGIN_USER_EMAIL) //
				.password(LOGIN_USER_PASSWORD) //
				.build();

			// test
			final var request = postRequest(LOGIN_PATH, requestBody);
			final var response = execute(request, HttpStatus.OK, AccessTokenResponse.class);

			// verify
			assertThat(response.getAccessToken()).isNotNull();
			assertThat(response.getTokenType()).isEqualTo("Bearer");
		}

		Stream<Arguments> 正_存在するユーザがログイン() {

			return Stream.of(
				// 管理者
				arguments(UserRoleEnum.ADMIN),
				// 一般ユーザ
				arguments(UserRoleEnum.MEMBER));
		}

		@Test
		void 異_存在しないユーザがログイン() throws Exception {
			// setup
			final var loginUser = UserSample.builder().build();

			// login request body
			final var requestBody = LoginRequest.builder() //
				.email(loginUser.getEmail()) //
				.password(loginUser.getPassword()) //
				.build();

			// login
			final var request = postRequest(LOGIN_PATH, requestBody);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER));
		}

		@Test
		void 異_パスワードが間違えている() throws Exception {
			// setup
			createLoginUser(UserRoleEnum.MEMBER);

			// login request body
			final var requestBody = LoginRequest.builder() //
				.email(LOGIN_USER_EMAIL) //
				.password(LOGIN_USER_PASSWORD + "dummy") //
				.build();

			// test
			final var request = postRequest(LOGIN_PATH, requestBody);
			execute(request, new UnauthorizedException(ErrorCode.WRONG_PASSWORD));
		}

	}

}
