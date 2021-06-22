package dev.abelab.crms.logic;

import org.springframework.stereotype.Component;

import lombok.*;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.repository.ReservationRepository;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.ForbiddenException;

@RequiredArgsConstructor
@Component
public class ReservationLogic {

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    /**
     * 編集権限があるか確認
     *
     * @param reservationId 予約ID
     *
     * @param userId        ユーザID
     */
    public void checkPermission(final int reservationId, final int userId) {
        final var reservation = this.reservationRepository.selectById(reservationId);
        final var user = this.userRepository.selectById(userId);

        // 管理者/予約者のみ編集可能
        if (user.getRoleId() != UserRoleEnum.ADMIN.getId() && !reservation.getUserId().equals(user.getId())) {
            throw new ForbiddenException(ErrorCode.USER_HAS_NO_PERMISSION);
        }
    }

}
