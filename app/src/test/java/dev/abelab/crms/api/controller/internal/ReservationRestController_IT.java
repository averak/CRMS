package dev.abelab.crms.api.controller.internal;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import java.util.Date;
import java.util.Calendar;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
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
import dev.abelab.crms.db.entity.ReservationSample;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.repository.ReservationRepository;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.api.request.ReservationCreateRequest;
import dev.abelab.crms.api.request.ReservationUpdateRequest;
import dev.abelab.crms.api.response.ReservationResponse;
import dev.abelab.crms.api.response.ReservationsResponse;
import dev.abelab.crms.util.DateTimeUtil;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.BaseException;
import dev.abelab.crms.exception.BadRequestException;
import dev.abelab.crms.exception.ConflictException;
import dev.abelab.crms.exception.NotFoundException;
import dev.abelab.crms.exception.ForbiddenException;

/**
 * ReservationRestController Integration Test
 */
public class ReservationRestController_IT extends AbstractRestController_IT {

	// API PATH
	static final String BASE_PATH = "/api/reservations";
	static final String GET_RESERVATIONS_PATH = BASE_PATH;
	static final String CREATE_RESERVATION_PATH = BASE_PATH;
	static final String UPDATE_RESERVATION_PATH = BASE_PATH + "/%d";
	static final String DELETE_RESERVATION_PATH = BASE_PATH + "/%d";

	static final Date TOMORROW = DateTimeUtil.getTomorrow();

	static final Date YESTERDAY = DateTimeUtil.getYesterday();

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
			final var credentials = getLoginUserCredentials(loginUser);

			// setup
			final var reservation1 = ReservationSample.builder().id(1).userId(loginUser.getId()).build();
			final var reservation2 = ReservationSample.builder().id(2).userId(loginUser.getId()).build();
			reservationRepository.insert(reservation1);
			reservationRepository.insert(reservation2);

			// test
			final var request = getRequest(GET_RESERVATIONS_PATH);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
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
			final var credentials = getLoginUserCredentials(loginUser);

			// request body
			final var requestBody = ReservationCreateRequest.builder() //
				.startAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 10)) //
				.finishAt(DateTimeUtil.addDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 11)) //
				.build();

			// test
			final var request = postRequest(CREATE_RESERVATION_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
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

		@ParameterizedTest
		@MethodSource
		void 異_無効な予約時間は予約不可(final Date date, final int startHour, final int finishHour, final BaseException exception) throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.MEMBER);
			final var credentials = getLoginUserCredentials(loginUser);

			// request body
			final var requestBody = ReservationCreateRequest.builder() //
				.startAt(DateTimeUtil.editDateTime(date, Calendar.HOUR_OF_DAY, startHour)) //
				.finishAt(DateTimeUtil.editDateTime(date, Calendar.HOUR_OF_DAY, finishHour)) //
				.build();

			// test
			final var request = postRequest(CREATE_RESERVATION_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, exception);
		}

		Stream<Arguments> 異_無効な予約時間は予約不可() {
			return Stream.of(
				// 過去の日時
				arguments(YESTERDAY, 10, 11, new BadRequestException(ErrorCode.PAST_RESERVATION_CANNOT_BE_CREATED)),
				// 開始時刻よりも前に終了時刻が設定されている
				arguments(TOMORROW, 11, 10, new BadRequestException(ErrorCode.INVALID_RESERVATION_TIME)),
				// 開始時刻と終了時刻が同じ
				arguments(TOMORROW, 10, 10, new BadRequestException(ErrorCode.INVALID_RESERVATION_TIME)),
				// 制限時間を超過している
				arguments(TOMORROW, 9, 22, new BadRequestException(ErrorCode.TOO_LONG_RESERVATION_HOURS)),
				// 予約可能範囲に収まっていない
				arguments(TOMORROW, 6, 8, new BadRequestException(ErrorCode.NOT_WITHIN_RESERVABLE_TIME_RANGE)),
				arguments(TOMORROW, 8, 10, new BadRequestException(ErrorCode.NOT_WITHIN_RESERVABLE_TIME_RANGE)),
				arguments(TOMORROW, 21, 23, new BadRequestException(ErrorCode.NOT_WITHIN_RESERVABLE_TIME_RANGE)),
				arguments(TOMORROW, 22, 23, new BadRequestException(ErrorCode.NOT_WITHIN_RESERVABLE_TIME_RANGE)));
		}

		@ParameterizedTest
		@MethodSource
		void 異_同時刻は既に予約済み(final int startHour, final int finishHour) throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.MEMBER);
			final var credentials = getLoginUserCredentials(loginUser);

			// 9~11時が既に予約済み
			final var reservation = ReservationSample.builder() //
				.userId(loginUser.getId()) //
				.startAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 12)) //
				.finishAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 14)) //
				.build();
			reservationRepository.insert(reservation);

			// request body
			final var requestBody = ReservationCreateRequest.builder() //
				.startAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, startHour)) //
				.finishAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, finishHour)) //
				.build();

			// test
			final var request = postRequest(CREATE_RESERVATION_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new ConflictException(ErrorCode.CONFLICT_RESERVATION_TIME));
		}

		Stream<Arguments> 異_同時刻は既に予約済み() {
			return Stream.of(
				// 開始時刻が重複
				arguments(13, 15),
				// 終了時刻が重複
				arguments(11, 13),
				// 開始時刻，終了時刻共に重複
				arguments(13, 14));

		}

	}

	/**
	 * 予約更新APIのテスト
	 */
	@Nested
	@TestInstance(PER_CLASS)
	class UpdateReservationsTest extends AbstractRestControllerInitialization_IT {

		@ParameterizedTest
		@MethodSource
		void 正_予約を更新(final UserRoleEnum userRole) throws Exception {
			// login user
			final var loginUser = createLoginUser(userRole);
			final var credentials = getLoginUserCredentials(loginUser);

			// setup
			final var reservation = ReservationSample.builder() //
				.userId(loginUser.getId()) //
				.startAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 10)) //
				.finishAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 11)) //
				.build();
			reservationRepository.insert(reservation);

			// request body
			final var requestBody = ReservationUpdateRequest.builder() //
				.startAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 13)) //
				.finishAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 14)) //
				.build();

			// test
			final var request = putRequest(String.format(UPDATE_RESERVATION_PATH, reservation.getId()), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, HttpStatus.OK);

			// verify
			final var createdReservation = reservationRepository.selectById(reservation.getId());
			assertThat(createdReservation.getUserId()).isEqualTo(loginUser.getId());
			assertThat(createdReservation.getStartAt()).isInSameMinuteAs(requestBody.getStartAt());
			assertThat(createdReservation.getFinishAt()).isInSameMinuteAs(requestBody.getFinishAt());
		}

		Stream<Arguments> 正_予約を更新() {
			return Stream.of(
				// 管理者
				arguments(UserRoleEnum.ADMIN),
				// 一般ユーザ
				arguments(UserRoleEnum.MEMBER));
		}

		@ParameterizedTest
		@MethodSource
		void 異_予約を更新する権限がない(final UserRoleEnum userRole, final boolean isReservationUser) throws Exception {
			// login user
			final var loginUser = createLoginUser(userRole);
			final var credentials = getLoginUserCredentials(loginUser);

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

			// request body
			final var requestBody = ReservationUpdateRequest.builder() //
				.startAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 10)) //
				.finishAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 11)) //
				.build();

			// test
			final var request = putRequest(String.format(UPDATE_RESERVATION_PATH, reservation.getId()), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION));
		}

		Stream<Arguments> 異_予約を更新する権限がない() {
			return Stream.of(
				// 一般ユーザ & 非予約者
				arguments(UserRoleEnum.MEMBER, false));
		}

		@Test
		void 異_更新対象予約が存在しない() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.ADMIN);
			final var credentials = getLoginUserCredentials(loginUser);

			// request body
			final var requestBody = ReservationUpdateRequest.builder() //
				.startAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 10)) //
				.finishAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 11)) //
				.build();

			// test
			final var request = putRequest(String.format(UPDATE_RESERVATION_PATH, 1), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_RESERVATION));
		}

		@Test
		void 正_過去の予約は更新不可() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.MEMBER);
			final var credentials = getLoginUserCredentials(loginUser);

			// setup
			final var reservation = ReservationSample.builder() //
				.userId(loginUser.getId()) //
				.startAt(DateTimeUtil.editDateTime(YESTERDAY, Calendar.HOUR_OF_DAY, 10)) //
				.finishAt(DateTimeUtil.editDateTime(YESTERDAY, Calendar.HOUR_OF_DAY, 11)) //
				.build();
			reservationRepository.insert(reservation);

			// request body
			final var requestBody = ReservationUpdateRequest.builder() //
				.startAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 13)) //
				.finishAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 14)) //
				.build();

			// test
			final var request = putRequest(String.format(UPDATE_RESERVATION_PATH, reservation.getId()), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new BadRequestException(ErrorCode.PAST_RESERVATION_CANNOT_BE_CHANGED));
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
			final var credentials = getLoginUserCredentials(loginUser);

			// setup
			final var reservation = ReservationSample.builder() //
				.userId(loginUser.getId()) //
				.startAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 10)) //
				.finishAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 11)) //
				.build();

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
			final var request = deleteRequest(String.format(DELETE_RESERVATION_PATH, reservation.getId()));
			request.header(HttpHeaders.AUTHORIZATION, credentials);
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
			final var credentials = getLoginUserCredentials(loginUser);

			// setup
			final var reservation = ReservationSample.builder() //
				.userId(loginUser.getId()) //
				.startAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 10)) //
				.finishAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 11)) //
				.build();

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
			final var request = deleteRequest(String.format(DELETE_RESERVATION_PATH, reservation.getId()));
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION));
		}

		Stream<Arguments> 異_予約を削除する権限がない() {
			return Stream.of(
				// 一般ユーザ & 非予約者
				arguments(UserRoleEnum.MEMBER, false));
		}

		@Test
		void 正_過去の予約は削除不可() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.MEMBER);
			final var credentials = getLoginUserCredentials(loginUser);

			// setup
			final var reservation = ReservationSample.builder() //
				.userId(loginUser.getId()) //
				.startAt(DateTimeUtil.editDateTime(YESTERDAY, Calendar.HOUR_OF_DAY, 10)) //
				.finishAt(DateTimeUtil.editDateTime(YESTERDAY, Calendar.HOUR_OF_DAY, 11)) //
				.build();
			reservationRepository.insert(reservation);

			// test
			final var request = deleteRequest(String.format(DELETE_RESERVATION_PATH, reservation.getId()));
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new BadRequestException(ErrorCode.PAST_RESERVATION_CANNOT_BE_DELETED));
		}

		@Test
		void 異_削除対象予約が存在しない() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.ADMIN);
			final var credentials = getLoginUserCredentials(loginUser);

			// test
			final var request = deleteRequest(String.format(DELETE_RESERVATION_PATH, 1));
			request.header(HttpHeaders.AUTHORIZATION, credentials);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_RESERVATION));
		}

	}

}
