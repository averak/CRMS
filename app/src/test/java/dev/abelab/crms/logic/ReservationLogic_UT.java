package dev.abelab.crms.logic;

import java.util.Date;
import java.util.Calendar;
import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.modelmapper.ModelMapper;

import dev.abelab.crms.db.entity.User;
import dev.abelab.crms.db.entity.UserSample;
import dev.abelab.crms.db.entity.Reservation;
import dev.abelab.crms.db.entity.ReservationSample;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.repository.ReservationRepository;
import dev.abelab.crms.util.DateTimeUtil;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.BaseException;
import dev.abelab.crms.exception.BadRequestException;
import dev.abelab.crms.exception.ConflictException;
import dev.abelab.crms.exception.ForbiddenException;

public class ReservationLogic_UT extends AbstractLogic_UT {

    static final Date TOMORROW = DateTimeUtil.getTomorrow();

    static final Date YESTERDAY = DateTimeUtil.getYesterday();

    @Injectable
    UserRepository userRepository;

    @Injectable
    ReservationRepository reservationRepository;

    @Tested
    ModelMapper modelMapper;

    @Tested
    ReservationLogic reservationLogic;

    /**
     * Test for check edit permission
     */
    @Nested
    @TestInstance(PER_CLASS)
    class CheckEditPermissionTest {

        @ParameterizedTest
        @MethodSource
        void 正_編集権限がある(final Reservation reservation, final User user) {
            new Expectations() {
                {
                    reservationRepository.selectById(anyInt);
                    result = reservation;
                }
                {
                    userRepository.selectById(anyInt);
                    result = user;
                }
            };

            // verify
            assertDoesNotThrow(() -> reservationLogic.checkEditPermission(reservation.getId(), user.getId()));
        }

        Stream<Arguments> 正_編集権限がある() {
            return Stream.of(
                // 管理者 & 予約者
                arguments( //
                    ReservationSample.builder().userId(SAMPLE_INT).build(), //
                    UserSample.builder().id(SAMPLE_INT).roleId(UserRoleEnum.ADMIN.getId()).build()),
                // 管理者 & 非予約者
                arguments( //
                    ReservationSample.builder().userId(SAMPLE_INT).build(), //
                    UserSample.builder().id(SAMPLE_INT + 1).roleId(UserRoleEnum.ADMIN.getId()).build()),
                // 一般ユーザ & 予約者
                arguments( //
                    ReservationSample.builder().userId(SAMPLE_INT).build(), //
                    UserSample.builder().id(SAMPLE_INT).roleId(UserRoleEnum.MEMBER.getId()).build()));
        }

        @ParameterizedTest
        @MethodSource
        void 異_編集権限がない(final Reservation reservation, final User user) {
            new Expectations() {
                {
                    reservationRepository.selectById(anyInt);
                    result = reservation;
                }
                {
                    userRepository.selectById(anyInt);
                    result = user;
                }
            };

            // verify
            final var exception =
                assertThrows(ForbiddenException.class, () -> reservationLogic.checkEditPermission(reservation.getId(), user.getId()));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_HAS_NO_PERMISSION);
        }

        Stream<Arguments> 異_編集権限がない() {
            return Stream.of(
                // 一般ユーザ & 非予約者
                arguments( //
                    ReservationSample.builder().userId(SAMPLE_INT).build(), //
                    UserSample.builder().id(SAMPLE_INT + 1).roleId(UserRoleEnum.MEMBER.getId()).build()));
        }

    }

    /**
     * Test for check deletable reservation
     */
    @Nested
    @TestInstance(PER_CLASS)
    class CheckDeletableReservationTest {

        @Test
        void 正_削除可能な予約かチェック() {
            final var reservation = ReservationSample.builder() //
                .userId(SAMPLE_INT) //
                .startAt(TOMORROW) //
                .finishAt(TOMORROW) //
                .build();

            // verify
            assertDoesNotThrow(() -> reservationLogic.checkDeletableReservation(reservation));
        }

        @Test
        void 異_過去の予約は削除不可() {
            final var reservation = ReservationSample.builder() //
                .userId(SAMPLE_INT) //
                .startAt(YESTERDAY) //
                .finishAt(YESTERDAY) //
                .build();

            // verify
            final var exception = assertThrows(BadRequestException.class, () -> reservationLogic.checkDeletableReservation(reservation));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.PAST_RESERVATION_CANNOT_BE_DELETED);

        }

    }

    /**
     * Test for validate reservation time
     */
    @Nested
    @TestInstance(PER_CLASS)
    class ValidateReservationTimeTest {

        @ParameterizedTest
        @MethodSource
        void 有効な予約時間かチェック(final Date date, final int startHour, final int startMinute, final int finishHour, final int finishMinute,
            final BaseException exception) {
            // given
            final var startAt = DateTimeUtil.editDateTimeHourAndMinute(date, startHour, startMinute);
            final var finishAt = DateTimeUtil.editDateTimeHourAndMinute(date, finishHour, finishMinute);

            // test & verify
            if (exception == null) {
                assertDoesNotThrow(() -> reservationLogic.validateReservationTime(startAt, finishAt, SAMPLE_INT, SAMPLE_INT));
            } else {
                final var occurredException = assertThrows(exception.getClass(),
                    () -> reservationLogic.validateReservationTime(startAt, finishAt, SAMPLE_INT, SAMPLE_INT));
                assertThat(occurredException.getErrorCode()).isEqualTo(exception.getErrorCode());

            }
        }

        Stream<Arguments> 有効な予約時間かチェック() {
            return Stream.of(
                // 有効
                arguments(TOMORROW, 10, 0, 16, 0, null), //
                arguments(TOMORROW, 9, 0, 11, 0, null), //
                arguments(TOMORROW, 20, 0, 22, 0, null), //
                // 無効
                // 過去の日時
                arguments(YESTERDAY, 10, 0, 11, 0, new BadRequestException(ErrorCode.PAST_RESERVATION_CANNOT_BE_CREATED)), //
                // 開始時刻よりも前に終了時刻が設定されている
                arguments(TOMORROW, 11, 0, 10, 0, new BadRequestException(ErrorCode.INVALID_RESERVATION_TIME)), //
                // 開始時刻と終了時刻が同じ
                arguments(TOMORROW, 10, 0, 10, 0, new BadRequestException(ErrorCode.INVALID_RESERVATION_TIME)), //
                // 制限時間を超過している
                arguments(TOMORROW, 9, 0, 22, 0, new BadRequestException(ErrorCode.TOO_LONG_RESERVATION_HOURS)), //
                // 予約可能範囲に収まっていない
                arguments(TOMORROW, 6, 0, 8, 59, new BadRequestException(ErrorCode.NOT_WITHIN_RESERVABLE_TIME_RANGE)), //
                arguments(TOMORROW, 8, 59, 11, 0, new BadRequestException(ErrorCode.NOT_WITHIN_RESERVABLE_TIME_RANGE)), //
                arguments(TOMORROW, 20, 0, 22, 1, new BadRequestException(ErrorCode.NOT_WITHIN_RESERVABLE_TIME_RANGE)), //
                arguments(TOMORROW, 22, 1, 23, 0, new BadRequestException(ErrorCode.NOT_WITHIN_RESERVABLE_TIME_RANGE)) //
            );
        }

        @ParameterizedTest
        @MethodSource
        void 異_同時刻は既に予約済み(final int startHour, final int finishHour) {
            final var reservation = ReservationSample.builder() //
                .userId(SAMPLE_INT) //
                .startAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 12)) //
                .finishAt(DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, 14)) //
                .build();

            new Expectations() {
                {
                    reservationRepository.selectByUserId(anyInt);
                    result = List.of(reservation);
                }
            };

            // verify
            final var startAt = DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, startHour);
            final var finishAt = DateTimeUtil.editDateTime(TOMORROW, Calendar.HOUR_OF_DAY, finishHour);
            final var exception =
                assertThrows(ConflictException.class, () -> reservationLogic.validateReservationTime(startAt, finishAt, SAMPLE_INT, 0));
            assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.CONFLICT_RESERVATION_TIME);
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
     * Test for get with user
     */
    @Nested
    @TestInstance(PER_CLASS)
    class GetWithUserTest {

        @Test
        void 正_予約からユーザを取得() {
            final var user = UserSample.builder().build();
            final var reservation = ReservationSample.builder().userId(SAMPLE_INT).build();

            new Expectations() {
                {
                    userRepository.selectById(anyInt);
                    result = user;
                }
            };

            // verify
            final var reservationWithUser = reservationLogic.getWithUser(reservation);
            assertThat(reservationWithUser.getUser()).isEqualTo(user);
        }

    }

    /**
     * Test for get next day reservations
     */
    @Nested
    @TestInstance(PER_CLASS)
    class GetNextDayReservationsTest {

        @Test
        void 正_翌日の予約一覧を取得() {
            final var user = UserSample.builder().build();

            // 予約一覧
            final var dayAfterTOMORROW = DateTimeUtil.addDateTime(TOMORROW, Calendar.DAY_OF_MONTH, 1);
            final List<Reservation> reservations = List.of( //
                // 過去の予約
                ReservationSample.builder().id(1).userId(user.getId()).startAt(YESTERDAY).finishAt(YESTERDAY).build(), //
                ReservationSample.builder().id(2).userId(user.getId()).startAt(YESTERDAY).finishAt(YESTERDAY).build(), //
                // 翌日の予約
                ReservationSample.builder().id(3).userId(user.getId()).startAt(TOMORROW).finishAt(TOMORROW).build(), //
                ReservationSample.builder().id(4).userId(user.getId()).startAt(TOMORROW).finishAt(TOMORROW).build(), //
                // 翌日以降の予約
                ReservationSample.builder().id(5).userId(user.getId()).startAt(dayAfterTOMORROW).finishAt(TOMORROW).build(), //
                ReservationSample.builder().id(6).userId(user.getId()).startAt(dayAfterTOMORROW).finishAt(TOMORROW).build() //
            );

            new Expectations() {
                {
                    reservationRepository.findAll();
                    result = reservations;
                }
                {
                    userRepository.selectById(anyInt);
                    result = user;
                }
            };

            // verify
            assertThat(reservationLogic.getNextDayReservations()) //
                .extracting("id", "user") //
                .containsExactlyInAnyOrder( //
                    tuple(reservations.get(2).getId(), user), //
                    tuple(reservations.get(3).getId(), user) //
                );
        }

    }

}
