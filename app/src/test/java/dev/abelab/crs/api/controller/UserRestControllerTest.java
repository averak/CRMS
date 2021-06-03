package dev.abelab.crs.api.controller;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

public class UserRestControllerTest extends AbstractRestControllerTest {

	/**
	 * ユーザ一覧取得APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetUsersTest {

		@Test
		void 正_管理者がユーザ一覧を取得() throws Exception {
		}

		@Test
		void 異_管理者以外はユーザ一覧を取得不可() throws Exception {
		}

	}

}
