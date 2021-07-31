package dev.abelab.crms.logic;

import java.util.Calendar;
import java.util.List;
import java.util.Arrays;
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
import dev.abelab.crms.property.CrmsProperty;
import dev.abelab.crms.util.DateTimeUtil;
import dev.abelab.crms.exception.ForbiddenException;

public class ReservationLogic_UT extends AbstractLogic_UT {

    @Injectable
    UserRepository userRepository;

    @Injectable
    ReservationRepository reservationRepository;

    @Injectable
    CrmsProperty crmsProperty;

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
            assertThrows(ForbiddenException.class, () -> reservationLogic.checkEditPermission(reservation.getId(), user.getId()));
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
     * Test for get next day reservations
     */
    @Nested
    @TestInstance(PER_CLASS)
    class GetNextDayReservationsTest {

        @Test
        void 正_翌日の予約一覧を取得() {
            final var user = UserSample.builder().build();

            final var yesterday = DateTimeUtil.getYesterday();
            final var tomorrow = DateTimeUtil.getTomorrow();
            final var dayAfterTomorrow = DateTimeUtil.addDateTime(tomorrow, Calendar.DAY_OF_MONTH, 1);

            // 予約一覧
            final List<Reservation> reservations = Arrays.asList( //
                // 過去の予約
                ReservationSample.builder().id(1).userId(user.getId()).startAt(yesterday).finishAt(yesterday).build(), //
                ReservationSample.builder().id(2).userId(user.getId()).startAt(yesterday).finishAt(yesterday).build(), //
                // 翌日の予約
                ReservationSample.builder().id(3).userId(user.getId()).startAt(tomorrow).finishAt(tomorrow).build(), //
                ReservationSample.builder().id(4).userId(user.getId()).startAt(tomorrow).finishAt(tomorrow).build(), //
                // 翌日以降の予約
                ReservationSample.builder().id(5).userId(user.getId()).startAt(dayAfterTomorrow).finishAt(tomorrow).build(), //
                ReservationSample.builder().id(6).userId(user.getId()).startAt(dayAfterTomorrow).finishAt(tomorrow).build() //
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
