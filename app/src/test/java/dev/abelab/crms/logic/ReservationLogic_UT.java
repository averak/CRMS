package dev.abelab.crms.logic;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.TestInstance.Lifecycle.*;
import static org.junit.jupiter.params.provider.Arguments.*;

import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.TestInstance;
import mockit.Expectations;
import mockit.Injectable;
import mockit.Tested;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

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
    ReservationLogic reservationLogic;

    /**
     * Test for check permission
     */
    @Nested
    @TestInstance(PER_CLASS)
    class CheckPermissionTest {

        @ParameterizedTest
        @MethodSource
        void 正_アクセス権限がある(final Reservation reservation, final User user) {
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
            assertDoesNotThrow(() -> reservationLogic.checkPermission(reservation.getId(), user.getId()));
        }

        Stream<Arguments> 正_アクセス権限がある() {
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
        void 異_アクセス権限がない(final Reservation reservation, final User user) {
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
            assertThrows(ForbiddenException.class, () -> reservationLogic.checkPermission(reservation.getId(), user.getId()));
        }

        Stream<Arguments> 異_アクセス権限がない() {
            return Stream.of(
                // 一般ユーザ & 非予約者
                arguments( //
                    ReservationSample.builder().userId(SAMPLE_INT).build(), //
                    UserSample.builder().id(SAMPLE_INT + 1).roleId(UserRoleEnum.MEMBER.getId()).build()));
        }

    }

}
