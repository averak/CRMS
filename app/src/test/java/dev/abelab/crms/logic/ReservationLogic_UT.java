package dev.abelab.crms.logic;

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
     * Test for find all with user
     */
    @Nested
    @TestInstance(PER_CLASS)
    class FindAllWithUserTest {

        @Test
        void 正_予約一覧を取得() {
            final var user = UserSample.builder().build();
            final var reservations = Arrays.asList( //
                ReservationSample.builder().id(1).userId(user.getId()).build(), //
                ReservationSample.builder().id(2).userId(user.getId()).build() //
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
            assertThat(reservationLogic.findAllWithUser()) //
                .extracting("id", "user") //
                .containsExactlyInAnyOrder( //
                    tuple(reservations.get(0).getId(), user), //
                    tuple(reservations.get(1).getId(), user) //
                );
        }

    }

}
