package dev.abelab.crms.api.controller;

import static java.lang.String.*;
import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.http.HttpStatus;

import dev.abelab.crms.db.entity.UserSample;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.api.request.UserCreateRequest;
import dev.abelab.crms.api.request.UserUpdateRequest;
import dev.abelab.crms.api.request.LoginUserUpdateRequest;
import dev.abelab.crms.api.response.UserResponse;
import dev.abelab.crms.api.response.UsersResponse;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.BaseException;
import dev.abelab.crms.exception.ConflictException;
import dev.abelab.crms.exception.NotFoundException;
import dev.abelab.crms.exception.ForbiddenException;
import dev.abelab.crms.exception.UnauthorizedException;

/**
 * UserRestController Integration Test
 */
public class UserRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/users";
	static final String GET_USERS_PATH = BASE_PATH;
	static final String CREATE_USER_PATH = BASE_PATH;
	static final String UPDATE_USER_PATH = BASE_PATH + "/%d";
	static final String DELETE_USER_PATH = BASE_PATH + "/%d";
	static final String GET_LOGIN_USER_PATH = BASE_PATH + "/me";
	static final String UPDATE_LOGIN_USER_PATH = BASE_PATH + "/me";

	@Autowired
	UserRepository userRepository;

	@Autowired
	PasswordEncoder passwordEncoder;

	/**
	 * ユーザ一覧取得APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetUsersTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_管理者がユーザ一覧を取得() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.ADMIN);
			final var jwt = getLoginUserJwt(loginUser);

			// setup
			final var user1 = UserSample.builder().id(1).email("email1").build();
			final var user2 = UserSample.builder().id(2).email("email2").build();
			userRepository.insert(user1);
			userRepository.insert(user2);

			// test
			final var request = getRequest(GET_USERS_PATH);
			request.header("Authorization", jwt);
			final var response = execute(request, HttpStatus.OK, UsersResponse.class);

			// verify
			assertThat(response.getUsers()) //
				.extracting(UserResponse::getId) //
				.containsExactly(loginUser.getId(), user1.getId(), user2.getId());
		}

		@Test
		void 異_管理者以外はユーザ一覧を取得不可() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.MEMBER);
			final var jwt = getLoginUserJwt(loginUser);

			// setup
			final var user1 = UserSample.builder().id(1).email("email1").build();
			final var user2 = UserSample.builder().id(2).email("email2").build();
			userRepository.insert(user1);
			userRepository.insert(user2);

			// test
			final var request = getRequest(GET_USERS_PATH);
			request.header("Authorization", jwt);
			execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION));
		}

	}

	/**
	 * ユーザ作成APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class CreateUserTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_管理者がユーザを作成() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.ADMIN);
			final var jwt = getLoginUserJwt(loginUser);

			// request body
			final var requestBody = UserCreateRequest.builder() //
				.firstName(SAMPLE_STR) //
				.lastName(SAMPLE_STR) //
				.password(SAMPLE_STR) //
				.email(SAMPLE_STR) //
				.roleId(UserRoleEnum.MEMBER.getId()) //
				.build();

			// test
			final var request = postRequest(CREATE_USER_PATH, requestBody);
			request.header("Authorization", jwt);
			execute(request, HttpStatus.CREATED);

			// verify
			final var createdUser = userRepository.selectByEmail(requestBody.getEmail());
			assertThat(createdUser) //
				.extracting("firstName", "lastName", "email", "roleId") //
				.containsExactly( //
					requestBody.getFirstName(), //
					requestBody.getLastName(), //
					requestBody.getEmail(), //
					requestBody.getRoleId());
			assertThat(passwordEncoder.matches(requestBody.getPassword(), createdUser.getPassword())).isTrue();
		}

		@Test
		void 異_管理者以外はユーザを作成不可() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.MEMBER);
			final var jwt = getLoginUserJwt(loginUser);

			// request body
			final var requestBody = UserCreateRequest.builder() //
				.firstName(SAMPLE_STR) //
				.lastName(SAMPLE_STR) //
				.password(SAMPLE_STR) //
				.email(SAMPLE_STR) //
				.roleId(UserRoleEnum.MEMBER.getId()) //
				.build();

			// test
			final var request = postRequest(CREATE_USER_PATH, requestBody);
			request.header("Authorization", jwt);
			execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION));
		}

		@Test
		void 異_メールアドレスが既に存在する() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.ADMIN);
			final var jwt = getLoginUserJwt(loginUser);

			// request body
			final var requestBody = UserCreateRequest.builder() //
				.firstName(SAMPLE_STR) //
				.lastName(SAMPLE_STR) //
				.password(SAMPLE_STR) //
				.email(SAMPLE_STR) //
				.roleId(UserRoleEnum.MEMBER.getId()) //
				.build();

			// test
			final var request = postRequest(CREATE_USER_PATH, requestBody);
			request.header("Authorization", jwt);
			execute(request, HttpStatus.CREATED);

			// verify
			execute(request, new ConflictException(ErrorCode.CONFLICT_EMAIL));
		}

		@Test
		void 異_無効なロールを付与() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.ADMIN);
			final var jwt = getLoginUserJwt(loginUser);

			// setup
			final var requestBody = UserCreateRequest.builder() //
				.firstName(SAMPLE_STR) //
				.lastName(SAMPLE_STR) //
				.password(SAMPLE_STR) //
				.email(SAMPLE_STR) //
				.roleId(0) //
				.build();

			// test
			final var request = postRequest(CREATE_USER_PATH, requestBody);
			request.header("Authorization", jwt);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_ROLE));
		}

	}

	/**
	 * ユーザ更新APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class UpdateUserTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_管理者がユーザを更新() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.ADMIN);
			final var jwt = getLoginUserJwt(loginUser);

			// setup
			final var user = UserSample.builder().build();
			userRepository.insert(user);

			// request body
			final var requestBody = UserUpdateRequest.builder() //
				.firstName(SAMPLE_STR + "XXX") //
				.lastName(SAMPLE_STR + "XXX") //
				.password(SAMPLE_STR + "XXX") //
				.email(SAMPLE_STR + "XXX") //
				.roleId(UserRoleEnum.MEMBER.getId()) //
				.build();

			// test
			final var request = putRequest(format(UPDATE_USER_PATH, user.getId()), requestBody);
			request.header("Authorization", jwt);
			execute(request, HttpStatus.OK);

			// verify
			final var updatedUser = userRepository.selectByEmail(requestBody.getEmail());
			assertThat(updatedUser) //
				.extracting("firstName", "lastName", "email", "roleId") //
				.containsExactly( //
					requestBody.getFirstName(), //
					requestBody.getLastName(), //
					requestBody.getEmail(), //
					requestBody.getRoleId());
			assertThat(passwordEncoder.matches(requestBody.getPassword(), updatedUser.getPassword())).isTrue();
		}

		@Test
		void 正_管理者以外はユーザを更新不可() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.MEMBER);
			final var jwt = getLoginUserJwt(loginUser);

			// setup
			final var user = UserSample.builder().build();
			userRepository.insert(user);

			// request body
			final var requestBody = UserUpdateRequest.builder() //
				.firstName(SAMPLE_STR + "XXX") //
				.lastName(SAMPLE_STR + "XXX") //
				.password(SAMPLE_STR + "XXX") //
				.email(SAMPLE_STR + "XXX") //
				.roleId(UserRoleEnum.MEMBER.getId()) //
				.build();

			// test
			final var request = putRequest(format(UPDATE_USER_PATH, user.getId()), requestBody);
			request.header("Authorization", jwt);
			execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION));
		}

		@Test
		void 異_更新対象ユーザが存在しない() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.ADMIN);
			final var jwt = getLoginUserJwt(loginUser);

			// request body
			final var requestBody = UserUpdateRequest.builder() //
				.firstName(SAMPLE_STR + "XXX") //
				.lastName(SAMPLE_STR + "XXX") //
				.password(SAMPLE_STR + "XXX") //
				.email(SAMPLE_STR + "XXX") //
				.roleId(UserRoleEnum.MEMBER.getId()) //
				.build();

			// test
			final var request = putRequest(format(UPDATE_USER_PATH, SAMPLE_INT), requestBody);
			request.header("Authorization", jwt);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER));
		}

	}

	/**
	 * ユーザ削除APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class DeleteUserTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_管理者がユーザを削除() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.ADMIN);
			final var jwt = getLoginUserJwt(loginUser);

			// setup
			final var user = UserSample.builder().build();
			userRepository.insert(user);

			// test
			final var request = deleteRequest(format(DELETE_USER_PATH, user.getId()));
			request.header("Authorization", jwt);
			execute(request, HttpStatus.OK);

			// verify
			final var exception = assertThrows(BaseException.class, () -> userRepository.selectById(user.getId()));
			assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_USER);
		}

		@Test
		void 異_管理者以外はユーザを削除不可() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.MEMBER);
			final var jwt = getLoginUserJwt(loginUser);

			// setup
			final var user = UserSample.builder().build();
			userRepository.insert(user);

			// test
			final var request = deleteRequest(format(DELETE_USER_PATH, user.getId()));
			request.header("Authorization", jwt);
			execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION));

			// verify
			assertDoesNotThrow(() -> userRepository.selectById(user.getId()));
		}

		@Test
		void 異_削除対象ユーザが存在しない() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.ADMIN);
			final var jwt = getLoginUserJwt(loginUser);

			// test
			final var request = deleteRequest(format(DELETE_USER_PATH, SAMPLE_INT));
			request.header("Authorization", jwt);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_USER));
		}

	}

	/**
	 * ログインユーザ詳細取得APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetLoginUserTest extends AbstractRestControllerInitialization_IT {

		@ParameterizedTest
		@MethodSource
		void 正_ログインユーザの詳細を取得(UserRoleEnum userRole) throws Exception {
			// login user
			final var loginUser = createLoginUser(userRole);
			final var jwt = getLoginUserJwt(loginUser);

			// test
			final var request = getRequest(GET_LOGIN_USER_PATH);
			request.header("Authorization", jwt);
			final var response = execute(request, HttpStatus.OK, UserResponse.class);

			assertThat(response) //
				.extracting("id", "firstName", "lastName", "email", "roleId") //
				.containsExactly( //
					loginUser.getId(), //
					loginUser.getFirstName(), //
					loginUser.getLastName(), //
					loginUser.getEmail(), //
					loginUser.getRoleId());
		}

		Stream<Arguments> 正_ログインユーザの詳細を取得() {
			return Stream.of(
				// 管理者
				arguments(UserRoleEnum.ADMIN),
				// 一般ユーザ
				arguments(UserRoleEnum.MEMBER));
		}

	}

	/**
	 * ログインユーザ更新APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class UpdateLoginUserTest extends AbstractRestControllerInitialization_IT {

		@Test
		void 正_ログインユーザを更新() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.ADMIN);
			final var jwt = getLoginUserJwt(loginUser);

			// request body
			final var requestBody = LoginUserUpdateRequest.builder() //
				.firstName(loginUser.getFirstName() + "XXX") //
				.lastName(loginUser.getLastName() + "XXX") //
				.currentPassword(LOGIN_USER_PASSWORD) //
				.newPassword(loginUser.getPassword() + "XXX") //
				.email(loginUser.getEmail() + "XXX") //
				.build();

			// test
			final var request = putRequest(UPDATE_LOGIN_USER_PATH, requestBody);
			request.header("Authorization", jwt);
			execute(request, HttpStatus.OK);

			// verify
			final var updatedUser = userRepository.selectByEmail(requestBody.getEmail());
			assertThat(updatedUser) //
				.extracting("firstName", "lastName", "email") //
				.containsExactly( //
					requestBody.getFirstName(), //
					requestBody.getLastName(), //
					requestBody.getEmail());
			assertThat(passwordEncoder.matches(requestBody.getNewPassword(), updatedUser.getPassword())).isTrue();
		}

		@Test
		void 異_現在のパスワードが間違えている() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.ADMIN);
			final var jwt = getLoginUserJwt(loginUser);

			// request body
			final var requestBody = LoginUserUpdateRequest.builder() //
				.firstName(loginUser.getFirstName() + "XXX") //
				.lastName(loginUser.getLastName() + "XXX") //
				.currentPassword(LOGIN_USER_PASSWORD + "miss") //
				.newPassword(loginUser.getPassword() + "XXX") //
				.email(loginUser.getEmail() + "XXX") //
				.build();

			// test
			final var request = putRequest(UPDATE_LOGIN_USER_PATH, requestBody);
			request.header("Authorization", jwt);
			execute(request, new UnauthorizedException(ErrorCode.WRONG_PASSWORD));
		}

	}

}
