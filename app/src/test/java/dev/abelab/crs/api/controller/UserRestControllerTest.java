package dev.abelab.crs.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.http.HttpStatus;

import dev.abelab.crs.db.entity.User;
import dev.abelab.crs.db.mapper.UserMapper;
import dev.abelab.crs.api.request.UserRequest;
import dev.abelab.crs.api.response.UserResponse;
import dev.abelab.crs.api.response.UsersResponse;

public class UserRestControllerTest extends AbstractRestControllerTest {

	// API PATH
	static final String BASE_PATH = "/api/users";
	static final String GET_USERS_PATH = BASE_PATH;

	@Autowired
	UserMapper userMapper;

	/**
	 * ユーザ一覧取得APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetUsersTest {

		@Test
		void 正_管理者がユーザ一覧を取得() throws Exception {
			final var user1 = User.builder() //
				.id(1) //
				.firstName(SAMPLE_STR) //
				.lastName(SAMPLE_STR) //
				.password(SAMPLE_STR) //
				.roleId(1) //
				.createdAt(new Date()) //
				.updatedAt(new Date()) //
				.build();
			final var user2 = User.builder() //
				.id(2) //
				.firstName(SAMPLE_STR) //
				.lastName(SAMPLE_STR) //
				.password(SAMPLE_STR) //
				.roleId(1) //
				.createdAt(new Date()) //
				.updatedAt(new Date()) //
				.build();
			userMapper.insert(user1);
			userMapper.insert(user2);

			/*
			 * test
			 */
			final var request = getRequest(GET_USERS_PATH);
			final var response = execute(request, HttpStatus.OK, UsersResponse.class);

			/*
			 * verify
			 */
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
		}

	}

}
