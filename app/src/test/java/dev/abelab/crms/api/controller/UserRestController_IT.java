package dev.abelab.crms.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;

import dev.abelab.crms.db.entity.UserSample;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.api.request.UserRequest;
import dev.abelab.crms.api.response.UserResponse;
import dev.abelab.crms.api.response.UsersResponse;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.ConflictException;

/**
 * UserRestController Integration Test
 */
public class UserRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/users";
	static final String GET_USERS_PATH = BASE_PATH;
	static final String CREATE_USER_PATH = BASE_PATH;
	static final UserRequest CREATE_USER_REQUEST = UserRequest.builder() //
		.firstName(SAMPLE_STR) //
		.lastName(SAMPLE_STR) //
		.password(SAMPLE_STR) //
		.email(SAMPLE_STR) //
		.roleId(UserRoleEnum.MEMBER.getId()) //
		.build();

	@Autowired
	UserRepository userRepository;

	/**
	 * ユーザ一覧取得APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetUsersTest {

		@Test
		void 正_管理者がユーザ一覧を取得() throws Exception {
			final var user1 = UserSample.builder().id(1).email("email1").build();
			final var user2 = UserSample.builder().id(2).email("email2").build();
			userRepository.insert(user1);
			userRepository.insert(user2);

			// send request
			final var request = getRequest(GET_USERS_PATH);
			final var response = execute(request, HttpStatus.OK, UsersResponse.class);

			// verify
			assertThat(response.getUsers()) //
				.extracting(UserResponse::getId) //
				.containsExactly(user1.getId(), user2.getId());
		}

		@Test
		void 異_管理者以外はユーザ一覧を取得不可() throws Exception {
		}

	}

	/**
	 * ユーザ作成APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class CreateUserTest {

		@Test
		void 正_ユーザを作成() throws Exception {
			// send request
			final var request = postRequest(CREATE_USER_PATH, CREATE_USER_REQUEST);
			execute(request, HttpStatus.CREATED);

			// verify
			final var createdUser = userRepository.selectByEmail(CREATE_USER_REQUEST.getEmail());
			assertThat(createdUser.getFirstName()).isEqualTo(CREATE_USER_REQUEST.getFirstName());
			assertThat(createdUser.getLastName()).isEqualTo(CREATE_USER_REQUEST.getLastName());
			assertThat(createdUser.getPassword()).isEqualTo(CREATE_USER_REQUEST.getPassword());
			assertThat(createdUser.getEmail()).isEqualTo(CREATE_USER_REQUEST.getEmail());
			assertThat(createdUser.getRoleId()).isEqualTo(CREATE_USER_REQUEST.getRoleId());
		}

		@Test
		void 異_メールアドレスが既に存在する() throws Exception {
			// send request
			final var request = postRequest(CREATE_USER_PATH, CREATE_USER_REQUEST);
			execute(request, HttpStatus.CREATED);

			// verify
			execute(request, new ConflictException(ErrorCode.CONFLICT_EMAIL));
		}

	}

}
