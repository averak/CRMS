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

import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.db.entity.UserSample;
import dev.abelab.crms.db.entity.Reservation;
import dev.abelab.crms.db.entity.ReservationSample;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.repository.ReservationRepository;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.api.request.ReservationCreateRequest;
import dev.abelab.crms.api.response.ReservationResponse;
import dev.abelab.crms.api.response.ReservationsResponse;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.ConflictException;
import dev.abelab.crms.exception.NotFoundException;
import dev.abelab.crms.exception.ForbiddenException;
import dev.abelab.crms.exception.UnauthorizedException;

/**
 * ReservationRestController Integration Test
 */
public class ReservationRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/reservations";
	static final String GET_RESERVATIONS_PATH = BASE_PATH;
	static final String CREATE_RESERVATIONS_PATH = BASE_PATH;
	static final String DELETE_RESERVATIONS_PATH = BASE_PATH + "/%d";

	@Autowired
	UserRepository userRepository;

	@Autowired
	ReservationRepository reservationRepository;

	/**
	 * 予約一覧取得APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class GetReservationsTest extends AbstractRestControllerInitialization_IT {

		@ParameterizedTest
		@MethodSource
		void 正_予約一覧を取得(final UserRoleEnum userRole) throws Exception {
			// login user
			final var loginUser = createLoginUser(userRole);
			final var jwt = getLoginUserJwt(loginUser);

			// setup
			final var reservation1 = ReservationSample.builder().id(1).userId(loginUser.getId()).build();
			final var reservation2 = ReservationSample.builder().id(2).userId(loginUser.getId()).build();
			reservationRepository.insert(reservation1);
			reservationRepository.insert(reservation2);

			// test
			final var request = getRequest(GET_RESERVATIONS_PATH);
			request.header("Authorization", jwt);
			final var response = execute(request, HttpStatus.OK, ReservationsResponse.class);

			// verify
			assertThat(response.getReservations()) //
				.extracting(ReservationResponse::getId) //
				.containsExactly(reservation1.getId(), reservation2.getId());
		}

		Stream<Arguments> 正_予約一覧を取得() {
			return Stream.of(
				// 管理者
				arguments(UserRoleEnum.ADMIN),
				// 一般ユーザ
				arguments(UserRoleEnum.MEMBER));
		}

	}

	/**
	 * 予約作成APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class CreateReservationsTest extends AbstractRestControllerInitialization_IT {

		@ParameterizedTest
		@MethodSource
		void 正_予約を作成(final UserRoleEnum userRole) throws Exception {
			// login user
			final var loginUser = createLoginUser(userRole);
			final var jwt = getLoginUserJwt(loginUser);

			// request body
			final var requestBody = ReservationCreateRequest.builder() //
				.startAt(SAMPLE_DATE) //
				.finishAt(SAMPLE_DATE) //
				.build();

			// test
			final var request = postRequest(CREATE_RESERVATIONS_PATH, requestBody);
			request.header("Authorization", jwt);
			execute(request, HttpStatus.CREATED);

			// verify
			final var createdReservations = reservationRepository.selectByUserId(loginUser.getId());
			assertThat(createdReservations.size()).isEqualTo(1);
			assertThat(createdReservations.get(0).getUserId()).isEqualTo(loginUser.getId());
			assertThat(createdReservations.get(0).getStartAt()).isInSameMinuteAs(requestBody.getStartAt());
			assertThat(createdReservations.get(0).getFinishAt()).isInSameMinuteAs(requestBody.getFinishAt());
		}

		Stream<Arguments> 正_予約を作成() {
			return Stream.of(
				// 管理者
				arguments(UserRoleEnum.ADMIN),
				// 一般ユーザ
				arguments(UserRoleEnum.MEMBER));
		}

		@Test
		void 異_指定時刻は既に予約済み() throws Exception {
			// FIXME
		}

	}

	/**
	 * 予約削除APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class DeleteReservationsTest extends AbstractRestControllerInitialization_IT {

		@ParameterizedTest
		@MethodSource
		void 正_予約を削除(final UserRoleEnum userRole, final boolean isReservationUser) throws Exception {
			// login user
			final var loginUser = createLoginUser(userRole);
			final var jwt = getLoginUserJwt(loginUser);

			// setup
			final var reservation = ReservationSample.builder().build();
			// 予約者かどうか
			if (isReservationUser) {
				reservation.setUserId(loginUser.getId());
			} else {
				final var reservationUser = UserSample.builder().id(loginUser.getId() + 1).build();
				userRepository.insert(reservationUser);
				reservation.setUserId(reservationUser.getId());
			}
			reservationRepository.insert(reservation);

			// test
			final var request = deleteRequest(format(DELETE_RESERVATIONS_PATH, reservation.getId()));
			request.header("Authorization", jwt);
			execute(request, HttpStatus.OK);

			// verify
			final var exception = assertThrows(NotFoundException.class, () -> reservationRepository.selectById(reservation.getId()));
			assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.NOT_FOUND_RESERVATION);
		}

		Stream<Arguments> 正_予約を削除() {
			return Stream.of(
				// 管理者 & 予約者
				arguments(UserRoleEnum.ADMIN, true),
				// 管理者 & 非予約者
				arguments(UserRoleEnum.ADMIN, false),
				// 一般ユーザ & 予約者
				arguments(UserRoleEnum.MEMBER, true));
		}

		@ParameterizedTest
		@MethodSource
		void 異_予約を削除する権限がない(final UserRoleEnum userRole, final boolean isReservationUser) throws Exception {
			// login user
			final var loginUser = createLoginUser(userRole);
			final var jwt = getLoginUserJwt(loginUser);

			// setup
			final var reservation = ReservationSample.builder().build();
			// 予約者かどうか
			if (isReservationUser) {
				reservation.setUserId(loginUser.getId());
			} else {
				final var reservationUser = UserSample.builder().id(loginUser.getId() + 1).build();
				userRepository.insert(reservationUser);
				reservation.setUserId(reservationUser.getId());
			}
			reservationRepository.insert(reservation);

			// test
			final var request = deleteRequest(format(DELETE_RESERVATIONS_PATH, reservation.getId()));
			request.header("Authorization", jwt);
			execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION));
		}

		Stream<Arguments> 異_予約を削除する権限がない() {
			return Stream.of(
				// 一般ユーザ & 非予約者
				arguments(UserRoleEnum.MEMBER, false));
		}

		@Test
		void 異_削除対象予約が存在しない() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.ADMIN);
			final var jwt = getLoginUserJwt(loginUser);

			// test
			final var request = deleteRequest(format(DELETE_RESERVATIONS_PATH, 1));
			request.header("Authorization", jwt);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_RESERVATION));
		}

	}

}
