package dev.abelab.crms.logic;

import java.util.Date;

import org.springframework.stereotype.Component;

import lombok.*;
import dev.abelab.crms.repository.UserRepository;
import dev.abelab.crms.repository.ReservationRepository;
import dev.abelab.crms.property.CrmsProperty;
import dev.abelab.crms.enums.UserRoleEnum;
import dev.abelab.crms.exception.ErrorCode;
import dev.abelab.crms.exception.BadRequestException;
import dev.abelab.crms.exception.ForbiddenException;

@RequiredArgsConstructor
@Component
public class ReservationLogic {

    private final UserRepository userRepository;

    private final ReservationRepository reservationRepository;

    private final CrmsProperty crmsProperty;

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

    /**
     * 予約時間のバリデーション
     */
    public void validateReservationTime(final Date startAt, final Date finishAt) {
        // 過去の日時
        final var now = new Date();
        if (now.after(startAt)) {
            throw new BadRequestException(ErrorCode.INVALID_RESERVATION);
        }

        // 開始時刻よりも前に終了時刻が設定されている
        if (startAt.after(finishAt)) {
            throw new BadRequestException(ErrorCode.INVALID_RESERVATION);
        }

        // 開始時刻と終了時刻が同じ
        if (startAt.equals(finishAt)) {
            throw new BadRequestException(ErrorCode.INVALID_RESERVATION);
        }

        // 制限時間を超過している
        final var diffHours = (finishAt.getTime() - startAt.getTime() ) /(1000 * 60 * 60);
        if (diffHours > this.crmsProperty.getReservableHours()) {
            throw new BadRequestException(ErrorCode.TOO_LONG_RESERVATION_HOURS);
        }
    }

}
