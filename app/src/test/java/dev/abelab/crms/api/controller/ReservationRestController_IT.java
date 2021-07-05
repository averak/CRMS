package dev.abelab.crms.api.controller;

import static java.lang.String.*;
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

import dev.abelab.crms.db.entity.UserSample;
import dev.abelab.crms.db.entity.ReservationSample;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.repository.ReservationRepository;
import dev.abelab.crms.property.CrmsProperty;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.api.request.ReservationCreateRequest;
import dev.abelab.crms.api.request.ReservationUpdateRequest;
import dev.abelab.crms.api.response.ReservationResponse;
import dev.abelab.crms.api.response.ReservationsResponse;
import dev.abelab.crms.exception.ErrorCode;
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

	@Autowired
	UserRepository userRepository;

	@Autowired
	ReservationRepository reservationRepository;

	@Autowired
	CrmsProperty crmsProperty;

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
			request.header(HttpHeaders.AUTHORIZATION, jwt);
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
			final var calendar1 = Calendar.getInstance();
			calendar1.setTime(SAMPLE_DATE);
			calendar1.add(Calendar.HOUR, 1);
			final var calendar2 = Calendar.getInstance();
			calendar2.setTime(SAMPLE_DATE);
			calendar2.add(Calendar.HOUR, 2);
			final var requestBody = ReservationCreateRequest.builder() //
				.startAt(calendar1.getTime()) //
				.finishAt(calendar2.getTime()) //
				.build();

			// test
			final var request = postRequest(CREATE_RESERVATION_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, jwt);
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
		void 異_開始時刻が終了時刻よりも後だと予約不可(final Date startAt, final Date finishAt) throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.MEMBER);
			final var jwt = getLoginUserJwt(loginUser);

			// request body
			final var requestBody = ReservationCreateRequest.builder() //
				.startAt(startAt) //
				.finishAt(finishAt) //
				.build();

			// test
			final var request = postRequest(CREATE_RESERVATION_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, jwt);
			execute(request, new BadRequestException(ErrorCode.INVALID_RESERVATION));
		}

		Stream<Arguments> 異_開始時刻が終了時刻よりも後だと予約不可() {
			return Stream.of(
				// 開始時刻よりも前に終了時刻が設定されている
				arguments(new Date(), SAMPLE_DATE),
				// 開始時刻と終了時刻が同じ
				arguments(SAMPLE_DATE, SAMPLE_DATE));
		}

		@Test
		void 異_過去の日時は予約不可() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.MEMBER);

			final var jwt = getLoginUserJwt(loginUser);

			// request body
			final var calendar1 = Calendar.getInstance();
			calendar1.set(2000, 1, 1, 9, 0);
			final var calendar2 = Calendar.getInstance();
			calendar2.set(2000, 1, 1, 12, 0);
			final var requestBody = ReservationCreateRequest.builder() //
				.startAt(calendar1.getTime()) //
				.finishAt(calendar2.getTime()) //
				.build();

			// test
			final var request = postRequest(CREATE_RESERVATION_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, jwt);
			execute(request, new BadRequestException(ErrorCode.INVALID_RESERVATION));
		}

		@Test
		void 異_予約時間が制限を超過している() throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.MEMBER);

			final var jwt = getLoginUserJwt(loginUser);

			// request body
			final var calendar1 = Calendar.getInstance();
			calendar1.setTime(SAMPLE_DATE);
			calendar1.add(Calendar.HOUR, 1);
			final var calendar2 = Calendar.getInstance();
			calendar2.setTime(SAMPLE_DATE);
			calendar2.add(Calendar.HOUR, 1 + crmsProperty.getReservableHours());
			calendar2.add(Calendar.MINUTE, 1);
			final var requestBody = ReservationCreateRequest.builder() //
				.startAt(calendar1.getTime()) //
				.finishAt(calendar2.getTime()) //
				.build();

			// test
			final var request = postRequest(CREATE_RESERVATION_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, jwt);
			execute(request, new BadRequestException(ErrorCode.TOO_LONG_RESERVATION_HOURS));
		}

		@ParameterizedTest
		@MethodSource
		void 異_同時刻は既に予約済み(final int startHour, final int finishHour) throws Exception {
			// login user
			final var loginUser = createLoginUser(UserRoleEnum.MEMBER);
			final var jwt = getLoginUserJwt(loginUser);

			// 9~13時は既に予約済み
			final var calendar1 = Calendar.getInstance();
			calendar1.setTime(SAMPLE_DATE);
			calendar1.add(Calendar.DATE, 1);
			calendar1.set(Calendar.HOUR, 9);
			final var calendar2 = Calendar.getInstance();
			calendar2.setTime(SAMPLE_DATE);
			calendar2.add(Calendar.DATE, 1);
			calendar2.set(Calendar.HOUR, 11);

			final var reservation = ReservationSample.builder().id(1).userId(loginUser.getId()).startAt(calendar1.getTime())
				.finishAt(calendar2.getTime()).build();
			reservationRepository.insert(reservation);

			// request body
			final var calendar3 = Calendar.getInstance();
			calendar3.setTime(SAMPLE_DATE);
			calendar3.add(Calendar.DATE, 1);
			calendar3.set(Calendar.HOUR, startHour);
			final var calendar4 = Calendar.getInstance();
			calendar4.setTime(SAMPLE_DATE);
			calendar4.add(Calendar.DATE, 1);
			calendar4.set(Calendar.HOUR, finishHour);
			final var requestBody = ReservationCreateRequest.builder() //
				.startAt(calendar3.getTime()) //
				.finishAt(calendar4.getTime()) //
				.build();

			// test
			final var request = postRequest(CREATE_RESERVATION_PATH, requestBody);
			request.header(HttpHeaders.AUTHORIZATION, jwt);
			execute(request, new ConflictException(ErrorCode.CONFLICT_RESERVATION_TIME));
		}

		Stream<Arguments> 異_同時刻は既に予約済み() {
			return Stream.of(
				// 開始時刻が重複
				arguments(10, 12),
				// 終了時刻が重複
				arguments(8, 10),
				// 開始時刻，終了時刻共に重複
				arguments(9, 11));
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
			final var jwt = getLoginUserJwt(loginUser);

			// setup
			final var startAtCalendar = Calendar.getInstance();
			startAtCalendar.setTime(SAMPLE_DATE);
			startAtCalendar.add(Calendar.DATE, 1);
			startAtCalendar.set(Calendar.HOUR, 9);
			final var finishAtCalendar = Calendar.getInstance();
			finishAtCalendar.setTime(SAMPLE_DATE);
			finishAtCalendar.add(Calendar.DATE, 1);
			finishAtCalendar.set(Calendar.HOUR, 11);

			final var reservation = ReservationSample.builder().id(1).userId(loginUser.getId()).startAt(startAtCalendar.getTime())
				.finishAt(finishAtCalendar.getTime()).build();
			reservationRepository.insert(reservation);

			// request body
			startAtCalendar.add(Calendar.DATE, 1);
			finishAtCalendar.add(Calendar.DATE, 1);
			final var requestBody = ReservationUpdateRequest.builder() //
				.startAt(startAtCalendar.getTime()) //
				.finishAt(finishAtCalendar.getTime()) //
				.build();

			// test
			final var request = putRequest(format(UPDATE_RESERVATION_PATH, reservation.getId()), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, jwt);
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

			// request body
			final var calendar1 = Calendar.getInstance();
			calendar1.setTime(SAMPLE_DATE);
			calendar1.add(Calendar.HOUR, 1);
			final var calendar2 = Calendar.getInstance();
			calendar2.setTime(SAMPLE_DATE);
			calendar2.add(Calendar.HOUR, 2);
			final var requestBody = ReservationUpdateRequest.builder() //
				.startAt(calendar1.getTime()) //
				.finishAt(calendar2.getTime()) //
				.build();

			// test
			final var request = putRequest(format(UPDATE_RESERVATION_PATH, reservation.getId()), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, jwt);
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
			final var jwt = getLoginUserJwt(loginUser);

			// request body
			final var calendar1 = Calendar.getInstance();
			calendar1.setTime(SAMPLE_DATE);
			calendar1.add(Calendar.HOUR, 1);
			final var calendar2 = Calendar.getInstance();
			calendar2.setTime(SAMPLE_DATE);
			calendar2.add(Calendar.HOUR, 2);
			final var requestBody = ReservationUpdateRequest.builder() //
				.startAt(calendar1.getTime()) //
				.finishAt(calendar2.getTime()) //
				.build();

			// test
			final var request = putRequest(format(UPDATE_RESERVATION_PATH, 1), requestBody);
			request.header(HttpHeaders.AUTHORIZATION, jwt);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_RESERVATION));
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
			final var request = deleteRequest(format(DELETE_RESERVATION_PATH, reservation.getId()));
			request.header(HttpHeaders.AUTHORIZATION, jwt);
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
			final var request = deleteRequest(format(DELETE_RESERVATION_PATH, reservation.getId()));
			request.header(HttpHeaders.AUTHORIZATION, jwt);
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
			final var request = deleteRequest(format(DELETE_RESERVATION_PATH, 1));
			request.header(HttpHeaders.AUTHORIZATION, jwt);
			execute(request, new NotFoundException(ErrorCode.NOT_FOUND_RESERVATION));
		}

	}

}
